package br.com.rehem.rodrigo.controlepatrimonial.repository.implement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Item;
import br.com.rehem.rodrigo.controlepatrimonial.domain.data.PageableCustom;
import br.com.rehem.rodrigo.controlepatrimonial.repository.ItemRepositoryCustom;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

	@PersistenceContext
    private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public Page<Item> buscarTodosItensDisponiveis2(PageableCustom pageable) 
	{
 		String ordery = this.getOrderBy("i",	pageable.getSort());
		
		StringBuffer where = new StringBuffer();
		where.append(" where ");
		where.append("  (  ");
		where.append("    (");
		where.append("        i.id not in (SELECT DISTINCT ism.id FROM Item ism inner join ism.movimentacaos m ) ");
		where.append("    ) ");
		where.append("    OR ");
		where.append("    ( i.id in ( ");
		where.append("					SELECT DISTINCT icm.id FROM Item icm inner join icm.movimentacaos m2 inner join m2.tipoMovimentacao tm WHERE  ");
		where.append("						tm.id = 2 AND");
		where.append("						m2.data = ( SELECT max(m3.data) from Movimentacao m3 inner join m3.items i2 WHERE i2.id = icm.id ) ");
		where.append("				 )");
		where.append("    ) ");
		where.append("  )   ");
		
		String filtroWhere = this.getFiltroWhere("i",	pageable.getFiltro());
		
		Query q = em.createQuery(" SELECT i FROM Item i "+where+filtroWhere+ordery);
		Query count = em.createQuery("Select count(*) from Item i "+where+filtroWhere);
		
		
		
		Long quantidadeItens = (Long) count.getSingleResult();
		
		q.setMaxResults(pageable.getPageSize());
		q.setFirstResult(pageable.getOffset());
		List<Item> itens = q.getResultList();
		Page<Item> pi = new PageImpl<>(itens, pageable, quantidadeItens);
		return pi;
	}
	
	//Monta os filtros da consulta principal
	private String getFiltroWhere(String tabelaName, HashMap<String, String> filtro) 
	{
		
		StringBuffer filtroWhere = new StringBuffer("");
		if(filtro == null)
		{
			return filtroWhere.toString();
		}
		if(filtro.containsKey("serial"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".serial like '%").append(filtro.get("serial")).append("%'");
		}
		
		if(filtro.containsKey("id"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".id =").append(filtro.get("id"));
		}
		
		if(filtro.containsKey("modelo"))
		{
			filtroWhere.append(" AND upper(").append(tabelaName).append(".modelo) like '%").append(filtro.get("modelo").toUpperCase()).append("%'");
		}
		
		if(filtro.containsKey("estado"))
		{
			filtroWhere.append(" AND upper(").append(tabelaName).append(".estado) like '%").append(filtro.get("estado").toUpperCase()).append("%'");
		}
		
		if(filtro.containsKey("numero"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".numero like '%").append(filtro.get("numero")).append("%'");
		}
		
		if(filtro.containsKey("tipoItem"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".tipoItem =").append(filtro.get("tipoItem"));
		}
		
		
		return filtroWhere.toString();
	}

	private String getOrderBy(String tabelaName, Sort s)
	{
		StringBuffer sf = new StringBuffer();
		Iterator<Order> ii =  s.iterator();
		while (ii.hasNext()) 
		{
			if(sf.length()<=0)
			{
				sf.append(" order by ");
			}else
			{
				sf.append(" , ");
			}
			Order o = ii.next();
			//order by item0_.serial asc, item
			if(o.getProperty().indexOf(",")>0)
			{
				sf.append(" "+tabelaName+"."+o.getProperty().substring(0, o.getProperty().indexOf(","))+" "+o.getDirection());
			}else{
				sf.append(" "+tabelaName+"."+o.getProperty()+" "+o.getDirection());
			}
		}
		return sf.toString();
	}
}