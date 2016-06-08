package br.com.rehem.rodrigo.controlepatrimonial.domain.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class PageableCustom implements Pageable, Serializable
{	

	private static final long serialVersionUID = 8820991416013209535L;
	private HashMap<String, String> filtro;
	private String search;
	
	private int page;
	private int size;
	private Sort sort;	
	
	private Pageable next;
	private Pageable previous;
	private Pageable first;
	
	
	@Override
	public Pageable next() {
		return next;
	}
	
	@Override
	public Pageable first() {
		return first;
	}
	
	public Pageable previous() {
		return previous;
	}
	
	@Override
	public int getPageNumber() {
		return page;
	}
	@Override
	public int getPageSize() {
		return size;
	}
	@Override
	public int getOffset() {
		return size*page;
	}

	@Override
	public Pageable previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}

	@Override
	public boolean hasPrevious() {
		return page > 0;
	}
	@Override
	public Sort getSort() 
	{
		return sort;
	}
	public void setSort(String sort) 
	{
		StringTokenizer st = new StringTokenizer(sort, ",");
		String prop = "";
		List<Order> os = new ArrayList<Order>();
		
		while (st.hasMoreElements()) 
		{
			String elem = (String) st.nextElement();
			if(elem.equalsIgnoreCase("ASC"))
			{
				os.add(new Order(Sort.Direction.ASC,prop));
			} else if(elem.equalsIgnoreCase("DESC"))
			{
				os.add(new Order(Sort.Direction.DESC,prop));
			}else
			{
				prop = elem;
			}
		}
		os.add(new Order(Sort.DEFAULT_DIRECTION,prop));
		
		Sort s = new Sort(os);
		this.sort = s;
	}
	public HashMap<String, String> getFiltro() {
		return filtro;
	}
	public void setFiltro(String filtro) 
	{
		
		this.filtro = new HashMap<String, String>();
		StringTokenizer token1 = new StringTokenizer(filtro, ",");
		//if(token1 != null)
		//{
			while(token1.hasMoreTokens())
			{
				String strTk = token1.nextToken();
				this.filtro.put(strTk.substring(0, strTk.indexOf(":")), strTk.substring(strTk.indexOf(":")+1,strTk.length()));
			}
			//this.filtro.keySet().forEach(chave -> System.out.println(chave));
		//}else{
			//this.filtro.put(filtro.substring(0, filtro.indexOf(":")), filtro.substring(filtro.indexOf(":"),filtro.length()-1));
		//}
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	public void setSize(int size) {
		this.size = size;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	
}
