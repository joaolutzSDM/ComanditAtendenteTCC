package br.com.alloy.comanditatendente.service.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.alloy.comanditatendente.ui.util.StringUtil;


public class ItemConta {

	private String label;
	private String value;
	
	//Default Constructor
	public ItemConta() {}
	
	public ItemConta(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
	public ItemConta(String label, BigDecimal value) {
		this(label, StringUtil.formatCurrencyValue(value));
	}
	
	public ItemConta(String label, Integer value) {
		this(label, value.toString());
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
