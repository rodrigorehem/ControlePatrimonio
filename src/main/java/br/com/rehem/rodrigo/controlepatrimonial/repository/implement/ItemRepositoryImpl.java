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
import br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO;
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

	@SuppressWarnings("unchecked")
	@Override
	public Page<ItemMovPessoaDTO> buscarTodosItensEntregue(PageableCustom pageable) 
	{
		String ordery = this.getOrderBy("i",	pageable.getSort());

		StringBuffer where = new StringBuffer();
		where.append("		 WHERE  ");
		where.append("			tm.id = 1 AND");
		where.append("			m2.data = ( SELECT max(m3.data) from Movimentacao m3 inner join m3.items i2 WHERE i2.id = i.id ) ");

		String filtroWhere = this.getFiltroWhere("i",	pageable.getFiltro());

		Query q = em.createQuery(" SELECT new br.com.rehem.rodrigo.controlepatrimonial.domain.dto.ItemMovPessoaDTO(i,m2,p) FROM Item i inner join i.movimentacaos m2 inner join m2.tipoMovimentacao tm inner join m2.pessoa p "+where+filtroWhere+ordery);
		Query count = em.createQuery(" SELECT count(*) FROM Item i inner join i.movimentacaos m2 inner join m2.tipoMovimentacao tm inner join m2.pessoa p "+where+filtroWhere);



		Long quantidadeItens = (Long) count.getSingleResult();

		q.setMaxResults(pageable.getPageSize());
		q.setFirstResult(pageable.getOffset());
		List<ItemMovPessoaDTO> itens = q.getResultList();
		Page<ItemMovPessoaDTO> pi = new PageImpl<>(itens, pageable, quantidadeItens);
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
		if(filtro.containsKey("i.serial"))
		{
			//filtroWhere.append(" AND ").append(tabelaName).append(".serial like '%").append(filtro.get("i.serial")).append("%'");
			filtroWhere.append(" AND upper(").append(tabelaName).append(".serial) like '%").append(filtro.get("i.serial").toUpperCase()).append("%'");
		}
		
		if(filtro.containsKey("i.tombo"))
		{
			filtroWhere.append(" AND upper(").append(tabelaName).append(".tombo) like '%").append(filtro.get("i.tombo").toUpperCase()).append("%'");
		}

		if(filtro.containsKey("i.id"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".id =").append(filtro.get("i.id"));
		}

		if(filtro.containsKey("i.modelo"))
		{
			filtroWhere.append(" AND upper(").append(tabelaName).append(".modelo) like '%").append(filtro.get("i.modelo").toUpperCase()).append("%'");
		}

		if(filtro.containsKey("i.estado"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".estado IN ('").append(filtro.get("i.estado").replaceAll("#", "','").toUpperCase()).append("') ");
		}

		if(filtro.containsKey("i.numero"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".numero like '%").append(filtro.get("i.numero")).append("%'");
		}

		if(filtro.containsKey("i.tipoItem"))
		{
			filtroWhere.append(" AND ").append(tabelaName).append(".tipoItem =").append(filtro.get("i.tipoItem"));
		}
		if(filtro.containsKey("p.nome"))
		{
			filtroWhere.append(" AND remove_acentos( upper(").append("p").append(".nome)) like remove_acentos('%").append(filtro.get("p.nome").toUpperCase()).append("%')");
		}
		
		if(filtro.containsKey("p.categoria_funcional"))
		{
			filtroWhere.append(" AND upper(").append("p").append(".categoriaFuncional) IN ('").append(filtro.get("p.categoria_funcional").replaceAll("#", "','").toUpperCase()).append("') ");
		}	


		return filtroWhere.toString();
	}

	private String getOrderBy(String tabelaName, Sort s)
	{
		StringBuffer sf = new StringBuffer();
		if(s!=null){
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
				if(o.getProperty().indexOf(".")<=0)
				{
					sf.append(tabelaName+".");
				}
				//order by item0_.serial asc, item
				if(o.getProperty().indexOf(",")>0)
				{
					sf.append(o.getProperty().substring(0, o.getProperty().indexOf(","))+" "+o.getDirection());
				}
				else{
					sf.append(o.getProperty()+" "+o.getDirection());
				}
			}
		}
		return sf.toString();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Item> findBySerial(String serial, Long tipoMovimentacao, Long pessoa, String tombo) 
	{
		serial = serial.trim().toUpperCase();
		tombo = tombo.trim().toUpperCase();
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT i FROM Item i where ");
		
		if(!serial.trim().equalsIgnoreCase(""))
		{
			sql.append(" upper(i.serial) like :serial AND ");
		}
		
		if(!tombo.trim().equalsIgnoreCase(""))
		{
			sql.append(" upper(i.tombo) like :tombo AND ");
		}
		sql.append("( ");
		
		if(!serial.trim().equalsIgnoreCase("") || !tombo.trim().equalsIgnoreCase(""))
		{
			sql.append("    (");
			sql.append("        i.id not in (SELECT DISTINCT ism.id FROM Item ism inner join ism.movimentacaos m ) ");
			sql.append("    ) ");
			sql.append("    OR ");
		}
		sql.append("    ( i.id in ( ");
		sql.append("					SELECT DISTINCT icm.id FROM Item icm inner join icm.movimentacaos m2 inner join m2.tipoMovimentacao tm inner join m2.pessoa p WHERE  ");
		sql.append("						tm.id = :tipoMovimentacao AND ");
		if(tipoMovimentacao.equals(1l))
		{
			sql.append("					p.id = :pessoa AND ");
		}
		sql.append("						m2.data = ( SELECT max(m3.data) from Movimentacao m3 inner join m3.items i2 WHERE i2.id = icm.id ) ");
		sql.append("				 )");
		sql.append("    ) ");
		sql.append(" )");
		Query q = em.createQuery(sql.toString());
		if(!serial.trim().equalsIgnoreCase(""))
		{
			q.setParameter("serial", "%"+serial+"%");
		}
		
		q.setParameter("tipoMovimentacao", tipoMovimentacao);
		
		if(tipoMovimentacao.equals(1l))
		{
			q.setParameter("pessoa", pessoa);
		}
		
		if(!tombo.trim().equalsIgnoreCase(""))
		{
			q.setParameter("tombo", "%"+tombo+"%");
		}
		
		List<Item> itens = q.getResultList();
		return itens;
	}
}