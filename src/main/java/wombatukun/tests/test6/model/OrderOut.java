package wombatukun.tests.test6.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

/**
 * Output order
 */
public class OrderOut {
	/** Идентификатор ордера */
	private Long id;
	/** Сумма ордера */
	private BigDecimal amount;
	/** Валюта суммы ордера / @JsonIgnore т.к. в примере выходных данных её нет:
	 * {“id”:1, ”amount”:100, ”comment”:”оплата заказа”, ”filename”:”orders.csv”, ”line”:1, ”result”:”OK” }
	 * */
	@JsonIgnore
	private String currency;
	/** Комментарий по ордеру */
	private String comment;
	/** Имя исходного файла */
	private String filename;
	/** Номер строки исходного файла */
	private Long line;
	/**
	 * Результат парсинга записи исходного файла:
	 * OK - если запись конвертирована корректно, или описание ошибки если запись не удалось конвертировать.
	 * */
	private String result;

	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public BigDecimal getAmount() { return amount; }

	public void setAmount(BigDecimal amount) { this.amount = amount; }

	public String getCurrency() { return currency; }

	public void setCurrency(String currency) { this.currency = currency; }

	public String getComment() { return comment; }

	public void setComment(String comment) { this.comment = comment; }

	public String getFilename() { return filename; }

	public void setFilename(String filename) { this.filename = filename; }

	public Long getLine() { return line; }

	public void setLine(Long line) { this.line = line; }

	public String getResult() { return result; }

	public void setResult(String result) { this.result = result; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OrderOut orderOut = (OrderOut) o;

		if (id != null ? !id.equals(orderOut.id) : orderOut.id != null) return false;
		if (amount != null ? !amount.equals(orderOut.amount) : orderOut.amount != null) return false;
		if (currency != null ? !currency.equals(orderOut.currency) : orderOut.currency != null) return false;
		if (comment != null ? !comment.equals(orderOut.comment) : orderOut.comment != null) return false;
		if (filename != null ? !filename.equals(orderOut.filename) : orderOut.filename != null) return false;
		if (line != null ? !line.equals(orderOut.line) : orderOut.line != null) return false;
		return result != null ? result.equals(orderOut.result) : orderOut.result == null;
	}

	@Override
	public int hashCode() {
		int result1 = id != null ? id.hashCode() : 0;
		result1 = 31 * result1 + (amount != null ? amount.hashCode() : 0);
		result1 = 31 * result1 + (currency != null ? currency.hashCode() : 0);
		result1 = 31 * result1 + (comment != null ? comment.hashCode() : 0);
		result1 = 31 * result1 + (filename != null ? filename.hashCode() : 0);
		result1 = 31 * result1 + (line != null ? line.hashCode() : 0);
		result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
		return result1;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("OrderOut{");
		sb.append("id=").append(id);
		sb.append(", amount=").append(amount);
		sb.append(", currency='").append(currency).append('\'');
		sb.append(", comment='").append(comment).append('\'');
		sb.append(", filename='").append(filename).append('\'');
		sb.append(", line=").append(line);
		sb.append(", result='").append(result).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
