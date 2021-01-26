package beanbags;

public class Beanbag {
	
	private int num;
	private String manufacturer;
	private String name;
	private String id;
	private short year;
	private byte month;
	private double price;
	private String freeText;
	private int reservationNumber;
	
	public Beanbag(int num, String manufacturer, String name, String id, short year, byte month) {
		this.num = num;
		this.manufacturer = manufacturer;
		this.name = name;
		this.id = id;
		this.year = year;
		this.month = month;
	}
	
	public Beanbag(int num, String manufacturer, String name, String id, short year, byte month, String freeText) {
		this.num = num;
		this.manufacturer = manufacturer;
		this.name = name;
		this.id = id;
		this.year = year;
		this.month = month;
		this.freeText = freeText;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(int reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

	public int getNum() {
		return num;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public short getYear() {
		return year;
	}

	public byte getMonth() {
		return month;
	}

	public String getFreeText() {
		return freeText;
	}	
	
	public void addQuantity(int num) {
		this.num += num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Beanbag [num=" + num + ", manufacturer=" + manufacturer + ", name=" + name + ", id=" + id + ", year="
				+ year + ", month=" + month + ", price=" + price + ", freeText=" + freeText + ", reservationNumber="
				+ reservationNumber + "]";
	}
}
