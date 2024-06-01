package models;

public class CupList {
	private String cupName;
	private int cupPrice;
	
	public CupList(String cupName, int cupPrice) {
		this.cupName = cupName;
		this.cupPrice = cupPrice;
	}

	public String getCupName() {
		return cupName;
	}

	public void setCupName(String cupName) {
		this.cupName = cupName;
	}

	public int getCupPrice() {
		return cupPrice;
	}

	public void setCupPrice(int cupPrice) {
		this.cupPrice = cupPrice;
	}
	
}
