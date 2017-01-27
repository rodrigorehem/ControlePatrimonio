package br.com.rehem.rodrigo.controlepatrimonial.domain.enumeration;

public enum CategoriaFuncional 
{
	JUIZ("Juiz(a).","Juiz"), DESEMBARGADOR("Des(a).","Desembargador"), SERVIDOR("Serv(a).","Servidor"), ASSISTENTE_MILITAR("ASSI. Militar","Assistente Militar");
	
	String sigla;
	String nomeFormatado;
	CategoriaFuncional(String sigla, String nomeFormatado)
	{
		this.sigla = sigla;
		this.nomeFormatado = nomeFormatado;
	}
	

}
