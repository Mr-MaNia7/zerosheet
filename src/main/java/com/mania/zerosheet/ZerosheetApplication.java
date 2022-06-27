package com.mania.zerosheet;

import com.mania.zerosheet.Agreement.Agreement;
import com.mania.zerosheet.Agreement.AgreementRepository;
import com.mania.zerosheet.Company.Company;
import com.mania.zerosheet.Company.CompanyRepository;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZerosheetApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZerosheetApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataSeederDemo(ItemRepository itemrepo, AgreementRepository agrepo, CompanyRepository compRepo ) {
		return args -> {
			itemrepo.save(new Item(
				"H frame 2.4 ( ኤች ፍሬም)", "PCS", 3.84, 2040.00, 3.50, 100
			));
			itemrepo.save(new Item(
				"X-bracing (ኤክስ መወጠሪያ)", "PCS", 3.84,374.00, 4.50, 120
			));
			itemrepo.save(new Item(
				"U-head (አናት)", "PCS", 4.45d, 595.00, 3.50d, 120
			));
			itemrepo.save(new Item( 
				"Base juck (እግር)", "PCS", 4.85d, 596.00 ,3.84d, 100
			));
			itemrepo.save(new Item(
				"Connector ( ማገናኛ)", "PCS", 4.45d, 107.00,3.55d, 100
			));
			itemrepo.save(new Item(
				"Extention1m (ማራዘሚያ1ሜ)", "PCS", 3.50d,766.00 , 3.50d, 120
			));
			itemrepo.save(new Item(
				"Extention50cm (ማራዘሚያ50ሤሜ)", "PCS", 2.85d, 767.00, 2.50d,80
			));
			itemrepo.save(new Item(
				"Clamp free (ክላምፕ ነጻ)", "PCS", 2.5d, 120.00, 2.80d, 70
			));
			itemrepo.save(new Item(
				"Clamp 90° (ክላምፕ 90°)", "PCS", 2.45d, 120.00, 3.50d, 90
			));
			itemrepo.save(new Item(
				"Clamp connector(ክላምፕ ማገናኛ)", "PCS", 1.45d, 120.00, 1.50d, 50
			));
			itemrepo.save(new Item(
				"CHSØ48*3mm 6m(ክብ ቱቦ 48*2)", "PCS", 5.50d, 150.00, 5.55d,120
			));
			itemrepo.save(new Item(
				"CHSØ48*2mm 2m(ክብ ቱቦ 48*2)", "PCS", 4.45d, 150.00, 3.50d,120
			));
			itemrepo.save(new Item(
				"Platform board(መቆሚያ ፓኔል)", "PCS", 8.5d, 3500.00, 7.50d,100
			));
			itemrepo.save(new Item(
				"Shoring Props (ቋሚ )", "PCS", 12.45d, 1200.00, 22.50d,120
			));
			itemrepo.save(new Item(
				"RHS 60*60*3(ሬክታንግል ቱቦ60*60*3)", "PCS", 11.22d, 150.00, 15.0d, 120
			));

			agrepo.save(new Agreement(
				"ሂ/ጠ/ሥ 0055/12", "የእስካፎልዲንግ ኪራይ ውል ስምምነት" 
			));

			compRepo.save(new Company(
				"ሒያ ጠቅላላ ስራ ተቋራጭ", "251911-216060", 5, "ቦሌ", "141", "አዲስ አበባ", "ኢትዮጵያ"
			));
			
		};
	}
}
