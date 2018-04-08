package wombatukun.tests.test6.model;

/**
 * Input order
 * orderId and amount are Strings for better (partial) deserialization json-files with jackson mapper.
 */
public class OrderIn {
	/** Идентификатор ордера */
	private String orderId;
	/** Сумма ордера */
	private String amount;
	/** Валюта суммы ордера */
	private String currency;
	/** Комментарий по ордеру */
	private String comment;

	public OrderIn() {
	}

	public OrderIn(String orderId, String amount, String currency, String comment) {
		this.orderId = orderId;
		this.amount = amount;
		this.currency = currency;
		this.comment = comment;
	}

	public String getOrderId() { return orderId; }

	public void setOrderId(String orderId) { this.orderId = orderId; }

	public String getAmount() { return amount; }

	public void setAmount(String amount) { this.amount = amount; }

	public String getCurrency() { return currency; }

	public void setCurrency(String currency) { this.currency = currency; }

	public String getComment() { return comment; }

	public void setComment(String comment) { this.comment = comment; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OrderIn orderIn = (OrderIn) o;

		if (orderId != null ? !orderId.equals(orderIn.orderId) : orderIn.orderId != null) return false;
		if (amount != null ? !amount.equals(orderIn.amount) : orderIn.amount != null) return false;
		if (currency != null ? !currency.equals(orderIn.currency) : orderIn.currency != null) return false;
		return comment != null ? comment.equals(orderIn.comment) : orderIn.comment == null;
	}

	@Override
	public int hashCode() {
		int result = orderId != null ? orderId.hashCode() : 0;
		result = 31 * result + (amount != null ? amount.hashCode() : 0);
		result = 31 * result + (currency != null ? currency.hashCode() : 0);
		result = 31 * result + (comment != null ? comment.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("OrderIn{");
		sb.append("orderId='").append(orderId).append('\'');
		sb.append(", amount='").append(amount).append('\'');
		sb.append(", currency='").append(currency).append('\'');
		sb.append(", comment='").append(comment).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
