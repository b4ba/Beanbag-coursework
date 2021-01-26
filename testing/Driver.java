package testing;

import java.io.IOException;

import beanbags.BeanBagIDNotRecognisedException;
import beanbags.BeanBagMismatchException;
import beanbags.BeanBagNotInStockException;
import beanbags.IllegalIDException;
import beanbags.IllegalNumberOfBeanBagsAddedException;
import beanbags.IllegalNumberOfBeanBagsReservedException;
import beanbags.IllegalNumberOfBeanBagsSoldException;
import beanbags.InsufficientStockException;
import beanbags.InvalidMonthException;
import beanbags.InvalidPriceException;
import beanbags.PriceNotSetException;
import beanbags.ReservationNumberNotRecognisedException;
import beanbags.Store;

public class Driver
{
	public static void main(String[] args) throws IllegalNumberOfBeanBagsAddedException,BeanBagMismatchException,
	IllegalIDException, InvalidMonthException, InvalidPriceException, InsufficientStockException,
	IllegalNumberOfBeanBagsSoldException, BeanBagNotInStockException, BeanBagIDNotRecognisedException,
	ReservationNumberNotRecognisedException, IllegalNumberOfBeanBagsReservedException, PriceNotSetException,
	IOException, ClassNotFoundException {

		Store firstStore = new Store();

		firstStore.addBeanBags(10, "manufact1", "bag1", "aaaaaaaa", (short) 2016, (byte) 3);
		firstStore.setBeanBagPrice("aaaaaaaa", 100);
		for (int i = 0; i < firstStore.inventory.size(); i ++) {
			System.out.println(firstStore.inventory.get(i) + ", ");
		}
		firstStore.addBeanBags(30, "manufact1", "bag1", "aaaaaaaa", (short) 2016, (byte) 3);
		firstStore.setBeanBagPrice("aaaaaaaa", 100);
		for (int i = 0; i < firstStore.inventory.size(); i ++) {
			System.out.println(firstStore.inventory.get(i) + ", ");
		}
		
//		firstStore.addBeanBags(10, "manufact2", "bag2", "bbbbbbbb", (short) 2016, (byte) 3);
//		firstStore.setBeanBagPrice("bbbbbbbb", 200);
//
//		firstStore.addBeanBags(10, "manufact3", "bag3", "cccccccc", (short) 2016, (byte) 3);
//		firstStore.setBeanBagPrice("cccccccc", 300);
//
//		firstStore.reserveBeanBags(5, "aaaaaaaa");
//		firstStore.reserveBeanBags(3, "bbbbbbbb");
//
//		firstStore.saveStoreContents("db.txt");
//
//		firstStore.loadStoreContents("db.txt");
//
//		System.out.println(firstStore.getNumberOfDifferentBeanBagsInStock());
//
//		for (int i = 0; i < firstStore.inventory.size(); i ++) {
//			System.out.println(firstStore.inventory.get(i) + ", ");
//		}
//		System.out.println();
//		firstStore.replace("aaaaaaaa", "ffffffff");
//		
//		for (int i = 0; i < firstStore.inventory.size(); i ++) {
//			System.out.println(firstStore.inventory.get(i) + ", ");
//		}
	}
}