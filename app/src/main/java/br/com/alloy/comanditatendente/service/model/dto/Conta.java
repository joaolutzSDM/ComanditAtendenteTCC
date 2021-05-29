package br.com.alloy.comanditatendente.service.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Conta {

	private List<ItemConta> itens;
	private BigDecimal valorTotal;
	
	/**
	 */
	public Conta() {
		this.itens = new ArrayList<>();
	}
	
	/**
	 * @param label
	 * @param value
	 */
	public void addItem(String label, BigDecimal value) {
		this.itens.add(new ItemConta(label, value));
	}
	
	/**
	 * @param label
	 * @param value
	 */
	public void addItem(String label, Integer value) {
		this.itens.add(new ItemConta(label, value));
	}
	
	/**
	 * @param label
	 * @param value
	 */
	public void addItem(String label, String value) {
		this.itens.add(new ItemConta(label, value));
	}
	
	/**
	 * @return the itens
	 */
	public List<ItemConta> getItens() {
		return itens;
	}
	/**
	 * @param itens the itens to set
	 */
	public void setItens(List<ItemConta> itens) {
		this.itens = itens;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

}
