package com.mania.zerosheet;

// import com.mania.zerosheet.Agreement.AgreementRepository;
// import com.mania.zerosheet.Company.Company;
// import com.mania.zerosheet.Company.CompanyRepository;
// import com.mania.zerosheet.Customers.CustomerRepository;
// import com.mania.zerosheet.ItemInstance.InstanceRepository;
// import com.mania.zerosheet.Items.Item;
// import com.mania.zerosheet.Items.ItemRepository;
// import com.mania.zerosheet.Transaction.TransactionRepository;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import com.mania.zerosheet.Customers.Customer;
// import com.mania.zerosheet.Transaction.Transaction;
// import java.util.Date;

@SpringBootApplication
public class ZerosheetApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZerosheetApplication.class, args);
	}

	// Commented for production release

	// @Bean
	// public CommandLineRunner dataSeeder(CustomerRepository custRepo, TransactionRepository transRepo,
	// 		ItemRepository itemrepo,
	// 		AgreementRepository agrepo, CompanyRepository compRepo, InstanceRepository instanceRepo) {
	// 	return args -> {
	// 		List<Item> items = new ArrayList<Item>();
	// 		Item item0 = new Item(
	// 				"H frame 2.4Ø50 (ኤች ፍሬም)", "PCS", 3.84, 2040.00, 3.50, 470);
	// 		Item item1 = new Item(
	// 				"H frame 2.4Ø42 (ኤች ፍሬም)", "PCS", 3.84, 2040.00, 3.50, 216);
	// 		Item item2 = new Item(
	// 				"H frame 2.4Ø42 wheel (ኤች ፍሬም ባለ ጎማ)", "PCS", 3.84, 2040.00, 3.50, 76);
	// 		Item item3 = new Item(
	// 				"X-bracing (ኤክስ መወጠሪያ)", "PCS", 3.84, 374.00, 4.50, 1105);
	// 		Item item4 = new Item(
	// 				"U-head (አናት)", "PCS", 4.45d, 595.00, 3.50d, 1214);
	// 		Item item5 = new Item(
	// 				"Base juck (እግር)", "PCS", 4.85d, 596.00, 3.84d, 0);
	// 		Item item6 = new Item(
	// 				"Connector (ማገናኛ)", "PCS", 4.45d, 107.00, 3.55d, 529);
	// 		Item item7 = new Item(
	// 				"Extention1m (ማራዘሚያ1ሜ)", "PCS", 3.50d, 766.00, 3.50d, 94);
	// 		Item item8 = new Item(
	// 				"Extention50cm (ማራዘሚያ50ሤሜ)", "PCS", 2.85d, 767.00, 2.50d, 84);
	// 		Item item9 = new Item(
	// 				"Clamp free (ክላምፕ ነጻ)", "PCS", 2.5d, 120.00, 2.80d, 839);
	// 		Item item10 = new Item(
	// 				"Clamp 90° (ክላምፕ 90°)", "PCS", 2.45d, 120.00, 3.50d, 12835);
	// 		Item item11 = new Item(
	// 				"Clamp connector (ክላምፕ ማገናኛ)", "PCS", 1.45d, 120.00, 1.50d, 1074);
	// 		Item item12 = new Item(
	// 				"CHSØ48*3mm 6m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d, 4);
	// 		Item item13 = new Item(
	// 				"CHSØ48*3mm 5m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d, 16);
	// 		Item item14 = new Item(
	// 				"CHSØ48*3mm 4.5m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d, 85);
	// 		Item item15 = new Item(
	// 				"CHSØ48*3mm 4m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d, 106);
	// 		Item item16 = new Item(
	// 				"CHSØ48*3mm 3.5m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d, 195);
	// 		Item item17 = new Item(
	// 				"CHSØ48*3mm 3m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d, 228);
	// 		Item item18 = new Item(
	// 				"CHSØ48*3mm 2m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d, 256);
	// 		Item item19 = new Item(
	// 				"Platform board (መቆሚያ ፓኔል)", "PCS", 8.5d, 3500.00, 7.50d, 11);
	// 		Item item20 = new Item(
	// 				"Shoring Props (ቋሚ )", "PCS", 12.45d, 1200.00, 22.50d, 2);
	// 		Item item21 = new Item(
	// 				"RHS 60*60*6 (ሬክታንግል ቱቦ60*60*6)", "PCS", 11.22d, 150.00, 15.0d, 250);
	// 		Item item22 = new Item(
	// 				"RHS 60*60*5 (ሬክታንግል ቱቦ60*60*5)", "PCS", 11.22d, 150.00, 15.0d, 7);
	// 		Item item23 = new Item(
	// 				"RHS 60*60*4 (ሬክታንግል ቱቦ60*60*4)", "PCS", 11.22d, 150.00, 15.0d, 255);
	// 		Item item24 = new Item(
	// 				"RHS 60*60*3 (ሬክታንግል ቱቦ60*60*3)", "PCS", 11.22d, 150.00, 15.0d, 661);
	// 		Item item25 = new Item(
	// 				"RHS 60*60*2 (ሬክታንግል ቱቦ60*60*2)", "PCS", 11.22d, 150.00, 15.0d, 193);
	// 		Item item26 = new Item(
	// 				"custer well", "PCS", 4.85d, 596.00, 3.84d, 11);
	// 		// Item item18 = new Item(
	// 		// "CHSØ48*2mm 2m(ክብ ቱቦ 48*2)", "PCS", 4.45d, 150.00, 3.50d, 120);

	// 		items.addAll(new AryList<Item>(Arrays.asList(
	// 				item0, item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12,
	// 				item13,
	// 				item14, item15, item16, item17, item18, item19, item20, item21, item22, item23, item24, item25,
	// 				item26)));
	// 		itemrepo.saveAll(items);
	// 		compRepo.save(new Company(
	// 				"ሒያ ጠቅላላ ስራ ተቋራጭ", "0911216060", "5", "ቦሌ", "141", "አዲስ አበባ", "ኢትዮጵያ"));

	// 		// // FOR TESTING
	// 		// // Customer customer = new Customer("A", "B", "C", "ascd@gmail.com",
	// 		// "0914233454", "1122", "AA", 12, 123,
	// 		// // 12345, 123, 12345, 12, 12, 12, 123, 123, 1, "Arada");

	// 		// // Transaction trans1 = new Transaction(10, new Date(), new Date(), 1L, 23,
	// 		// 21, 12, customer, item1);
	// 		// // trans1.setCustomer(customer);

	// 		// // customer.addTransaction(trans1);
	// 		// // transRepo.save(trans1);

	// 		// // custRepo.save(customer);
	// 	};
	// }

}
