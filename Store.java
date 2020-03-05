package beanbags;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Store implements BeanBagStore {

	public ObjectArrayList inventory = new ObjectArrayList();
	private ObjectArrayList reservations = new ObjectArrayList();
	private ObjectArrayList sold = new ObjectArrayList();
	private int stockTotal = 0;

	@Override
	public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month)
			throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException, IllegalIDException,
			InvalidMonthException {
		// Error Checking
		// Check if the month is between 1 and 12
		if (month < 1 || month > 12) throw new InvalidMonthException("Inexistent month");
		// Check if you're adding negative stock
		if (num < 0) throw new IllegalNumberOfBeanBagsAddedException("Attempt to add negative amount of stock");
		// Check if it's the right length
		if (id.length() != 8) throw  new  IllegalIDException("Wrong amount of characters");
		// Check if it's in hexadecimal
		try {long hexID = Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		
		//Adding Beanbags
		Beanbag existingBeanbag = findFirstItem(id, inventory);
		//If beanbag added already exists, add the quantities together.
		if (existingBeanbag != null) {
			existingBeanbag.addQuantity(num);
		} else {
			//If not simply add the new beanbag to inventory
			inventory.add(new Beanbag(num, manufacturer, name, id, year, month));
		}
		stockTotal += num;
	}

	@Override
	public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month,
			String information) throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException,
	IllegalIDException, InvalidMonthException {
		// Error Checking
		// Check if the month is between 1 and 12
		if (month < 1 || month > 12) throw new InvalidMonthException("Inexistent month");
		// Check if you're adding negative stock
		if (num < 0) throw new IllegalNumberOfBeanBagsAddedException("Attempt to add negative amount of stock");
		// Check if it's the right length
		if (id.length() != 8) throw  new  IllegalIDException("Wrong amount of characters");
		// Check if it's in hexadecimal
		try {long hexID = Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}

		//Adding Beanbags
		Beanbag existingBeanbag = findFirstItem(id, inventory);
		//If beanbag added already exists, add the quantities together.
		if (existingBeanbag != null) {
			existingBeanbag.addQuantity(num);
		} else {
			//If not simply add the new beanbag to inventory
			inventory.add(new Beanbag(num, manufacturer, name, id, year, month, information));
		}
		stockTotal += num;
	}

	@Override
	public void setBeanBagPrice(String id, int priceInPence)
			throws InvalidPriceException, BeanBagIDNotRecognisedException, IllegalIDException {
		//Error Checking
		//Check if price is a negative value.
		if (priceInPence < 0) throw new InvalidPriceException("Price cannot be a negative value");
		Beanbag searchItem = findFirstItem(id, inventory);
		try {Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		//Check if beanbag ID is a valid hexadecimal value
		if(searchItem == null) throw new BeanBagIDNotRecognisedException("Beanbag ID could not be matched");

		//Set the price for the searched beanbag.
		searchItem.setPrice(priceInPence/100);
	}

	@Override
	public void sellBeanBags(int num, String id) //CHECK
			throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsSoldException,
			PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
		//Error Checking
		//Check if stock being added is less than 1
		if (num < 1) throw new IllegalNumberOfBeanBagsSoldException("Attempt to add negative amount of stock");
		try {Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		Beanbag searchItem = findFirstItem(id, inventory);
		//Check if ID of beanbag is a valid hexadecimal and not null
		if (searchItem == null) throw new BeanBagIDNotRecognisedException("Beanbag with ID" + id + " could not be found");
		//Check if quantity being sold is less than what is available in stock
		if (searchItem.getNum() < num) throw new InsufficientStockException("Attempt to sell more than is in stock");
		//Check if the beanbag searched has a price set
		if (searchItem.getNum() > 0 && searchItem.getPrice() == 0.0) throw new PriceNotSetException("No Price Set for "+searchItem);

		//Implementation
		Beanbag itemInSold = findFirstItem(id, sold);
		//Set number of beanbag in sold list to the quantity sold if it's not equal to 0.
		if (itemInSold != null) {
			itemInSold.setNum(itemInSold.getNum() + num);
		} else {
			//Add beanbag to sold list
			sold.add(new Beanbag(num, searchItem.getManufacturer(), searchItem.getName(), searchItem.getId(), searchItem.getYear(), searchItem.getMonth(), searchItem.getFreeText()));
		}
		//Reduce the quantity of sold beanbag in the list of available beanbags.
		searchItem.setNum(searchItem.getNum() - num);
		stockTotal -= num;
	}

	@Override
	public int reserveBeanBags(int num, String id)
			throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsReservedException,
			PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
		//Error Checking
		//Check if quantity is less than 1.
		if (num < 1) throw new IllegalNumberOfBeanBagsReservedException("Attempt to reserve a negative amount of stock");
		try {Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		Beanbag searchItem = (Beanbag) findFirstItem(id, inventory);
		//Check if ID of beanbag is a valid hexadecimal and not null
		if (searchItem == null) throw new BeanBagIDNotRecognisedException("Beanbag with ID" + id + " could not be found");
		//Check is quantity sold is less than available quantity
		if (searchItem.getNum() < num) throw new InsufficientStockException("Attempt to sell more than is in stock");
		//Check if the beanbag searched has a price set 
		if (searchItem.getNum() > 0 && searchItem.getPrice() == 0.0) throw new PriceNotSetException("No Price Set for "+searchItem);

		//Generate unique hexadecimal ID for each beanbag
		int generatedKey = Integer.parseInt((Math.random()+"").substring(2, 6));
		//Initialize new beanbag object
		Beanbag newReservation = new Beanbag(num, searchItem.getManufacturer(), searchItem.getName(), searchItem.getId(), searchItem.getYear(), searchItem.getMonth(), searchItem.getFreeText());
		//Set generated ID to reserved beanbag
		newReservation.setReservationNumber(generatedKey);
		//Add beanbag to the reservations list
		reservations.add(newReservation);
		//Decrease the quantity of reserved beanbag from available stock
		searchItem.setNum(searchItem.getNum() - num);

		return generatedKey;
	}

	@Override
	public void unreserveBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {
		//Iterate through reservations list.
		for (int i = 0; i < reservations.size(); i++) {
			//If reservation ID of beanbag matches reservation ID in list, remove it.
			if (((Beanbag)reservations.get(i)).getReservationNumber() == reservationNumber) {
				reservations.remove(i);
			}
		}
	}

	@Override
	public void sellBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {
		//Iterate through reservations list.
		for (int i = 0; i < reservations.size(); i++) {
			//If reservation ID of beanbag matches reservation ID in list, add it to sold list.
			if (((Beanbag)reservations.get(i)).getReservationNumber() == reservationNumber) {
				sold.add(reservations.get(i));
			}
		}
	}

	@Override
	public int beanBagsInStock() {
		//Return available beanbags
		return stockTotal;
	}

	@Override
	public int reservedBeanBagsInStock() {
		//Return amount of reserved beanbags
		return reservations.size();
	}

	@Override
	public int beanBagsInStock(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
		try {Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		Beanbag searchItem = (Beanbag) findFirstItem(id, inventory);
		//Check if ID of beanbag is a valid hexadecimal and not null
		if (searchItem == null) throw new BeanBagIDNotRecognisedException("Beanbag with ID" + id + " could not be found");

		//Return quantity of searched beanbag from inventory.
		return searchItem.getNum();
	}

	@Override
	public void saveStoreContents(String filename) throws IOException {
		//Initialize & instantiate object 
		FileWriter fr = new FileWriter(filename);

		// Iterate through inventory ArrayList 
		for (int i = 0; i < inventory.size(); i++) {
			Beanbag searchItem = (Beanbag)inventory.get(i);
			//Write all beanbags (with details) to the given file
			fr.write(searchItem.getNum()+", "+searchItem.getManufacturer()+", "+searchItem.getName()+", "+searchItem.getId()+", "+searchItem.getYear()+", "+searchItem.getMonth()+", "+searchItem.getPrice()+", "+searchItem.getFreeText()+"\n"); //to be finished
		}

		fr.flush();
		fr.close();
	}

	@Override
	public void loadStoreContents(String filename) throws IOException, ClassNotFoundException {
		
		//Initialize & instantiate object
		BufferedReader br = new BufferedReader(new FileReader(filename));

		String currentLine = br.readLine();
		//Load current inventory items in an array from given file.
		while (currentLine != null) {
			String[] arr = currentLine.split(", ");
			try {
				addBeanBags(Integer.parseInt(arr[0]), arr[1], arr[2], arr[3], Short.parseShort(arr[4]), Byte.parseByte(arr[5]), arr[7]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalNumberOfBeanBagsAddedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BeanBagMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalIDException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidMonthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentLine = br.readLine();
		}

		br.close();

	}

	@Override
	public int getNumberOfDifferentBeanBagsInStock() {
		//Return the size of the inventory
		return inventory.size();
	}

	@Override
	public int getNumberOfSoldBeanBags() {
		//Declare variable
		int beanbagNumber = 0;
		//Iterate through list of sold beanbags
		for (int i = 0; i < sold.size(); i ++) {
			//Get quantity of sold beanbags and add to declared variable.
			Beanbag item = (Beanbag)sold.get(i);
			beanbagNumber+= item.getNum();
		}
		//Return variable.
		return beanbagNumber;
	}

	@Override
	public int getNumberOfSoldBeanBags(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
		try {Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		Beanbag searchItem = (Beanbag) findFirstItem(id, sold);
		//Error Checking
		//Check if ID of beanbag is a valid hexadecimal and not null
		if (searchItem == null) throw new BeanBagIDNotRecognisedException("Beanbag with ID" + id + " could not be found");
		int counter = 0;
		//Iterate through list of sold beanbags
		for (int i = 0; i < sold.size(); i ++) {
			//Get quantity of sold beanbags and add to declared variable.
			Beanbag item = (Beanbag)sold.get(i);
			//Only if IDs match add to counter
			if (item.getId().equals(id)) {
				counter+= item.getNum();
			}
		}
		return counter;
	}

	@Override
	public int getTotalPriceOfSoldBeanBags() {
		double price = 0.0;
		//Iterate through sold beanbags list
		for (int i = 0; i < sold.size(); i++) {
			Beanbag element = ((Beanbag)sold.get(i));
			//Get price of each beanbag and multiply by corresponding quantity.
			//Then add to the price variable
			price += element.getPrice() * element.getNum();
		}
		//Convert price to pence and return
		int priceInPence = (int) (price * 100);
		return priceInPence;
	}

	@Override
	public int getTotalPriceOfSoldBeanBags(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
		double price = 0;
		//Iterate through sold beanbags list
		for (int i = 0; i < sold.size(); i++) {
			Beanbag searchItem = ((Beanbag) sold.get(i));
			//If IDs match get the price of each beanbag and add it to price variable
			if (searchItem.getId().equals(id)) {
				price += searchItem.getPrice();
			}
		}
		//Convert total price to pence and return it.
		int priceInPence = (int) (price * 100);
		return priceInPence;
	}

	@Override
	public int getTotalPriceOfReservedBeanBags() {
		//Declare double variable
		double price = 0;
		//Iterate through reservations list
		for (int i = 0; i < reservations.size(); i++) {
			Beanbag searchItem = ((Beanbag) reservations.get(i));
			//Get the price for each beanbag in list and add to variable.
			price += searchItem.getPrice();
		}
		//Convert price to pence and return.
		int priceInPence = (int) (price * 100);
		return priceInPence;
	}

	@Override
	public String getBeanBagDetails(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
		//Error Checking
		//Check if ID of beanbag is a valid hexadecimal and not null
		try {Long.parseLong(id, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		Beanbag searchItem = (Beanbag) findFirstItem(id, inventory);
		if (searchItem == null) throw new BeanBagIDNotRecognisedException("Beanbag with ID" + id + " could not be found");
		//Return the description of requested beanbag
		return searchItem.getFreeText();
	}

	@Override
	public void empty() {
		//Reset inventory and reservations ArrayLists
		inventory = new ObjectArrayList();
		reservations = new ObjectArrayList();
	}

	@Override
	public void resetSaleAndCostTracking() {
		//Reset total stock and the sold list
		stockTotal = 0;
		sold = new ObjectArrayList();
	}

	@Override
	public void replace(String oldId, String replacementId) throws BeanBagIDNotRecognisedException, IllegalIDException {
		try {Long.parseLong(oldId, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}
		try {Long.parseLong(replacementId, 16);} catch (NumberFormatException e) {throw new IllegalIDException(e+"");}

		//Iterate through inventory ArrayList
		for (int i = 0; i < inventory.size(); i ++) {
			Beanbag searchItem = (Beanbag) inventory.get(i);
			//If inputed replacement ID matches beanbag ID output error message. 
			if (searchItem.getId().equals(replacementId)) {
				throw new IllegalIDException("Replacement ID already exists");
			}
		}
		//Iterate through inventory ArrayList
		for (int i = 0; i < inventory.size(); i ++) {
			Beanbag searchItem = (Beanbag) inventory.get(i);
			//Update current ID with inputed ID
			if (searchItem.getId().equals(oldId)) {
				searchItem.setId(replacementId);
			}
		}
		//Iterate through sold ArrayList
		for (int i = 0; i < sold.size(); i ++) {
			Beanbag searchItem = (Beanbag) sold.get(i);
			//Update current ID with inputed ID
			if (searchItem.getId().equals(oldId)) {
				searchItem.setId(replacementId);
			}
		}
		//Iterate through reservations ArrayList
		for (int i = 0; i < reservations.size(); i ++) {
			Beanbag searchItem = (Beanbag) reservations.get(i);
			//Update current ID with inputed ID
			if (searchItem.getId().equals(oldId)) {
				searchItem.setId(replacementId);
			}
		}
	}

	private Beanbag findFirstItem(String id, ObjectArrayList inventory){
		//Iterate through inventory ArryList
		for (int i = 0; i < inventory.size(); i++) {
			Beanbag searchItem = (Beanbag) inventory.get(i);
			//If ID of searched beanbag matches an ID in inventory, return beanbag.
			if (searchItem.getId().equals(id)) return searchItem;
		}
		return null;
	}

}
