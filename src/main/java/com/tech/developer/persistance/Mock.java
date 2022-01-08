package com.tech.developer.persistance;

import java.util.List;

import com.tech.developer.domain.Person;
import com.tech.developer.domain.Pin;

/**
 * 
 * The Mock is used to add some dummy data into the database when is needed
 * 
 * @author yfabio
 *
 */
public class Mock {

	private static DAO<Pin> pindao;
	private static DAO<Person> persondao;

	static {
		try {
			persondao = DAOPerson.getInstance();
			pindao = DAOPin.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void load() throws PersistanceException {

		try {

			new Thread(() -> {

				try {
					
					List<Person> people = persondao.get(SQLCriteriaFactory.getPersons(), List.of());
									
					for (Person p : people) {
						System.out.println(p);
						System.out.println();
						List<Pin> pins = pindao.get(SQLCriteriaFactory.getPinsByPersonID(),List.of(p.getId()));
						pins.forEach(e -> {System.out.println(e);});
						System.out.println();											
					}
				

				} catch (PersistanceException e2) {
					throw new RuntimeException(e2);
				}

			}).start();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static void exec() throws PersistanceException {

		try {
			Person p = new Person();
			p.setUsername("zoe");
			p.setPassword("senha");
			p.setLogged(true);

			persondao.insert(p);

			List<Object> param = List.of(p.getUsername());

			p = persondao.get(SQLCriteriaFactory.getPersonByUsername(), param).stream().findFirst().orElse(new Person());

			Pin p1 = new Pin();
			p1.setTitle("Amazon");
			p1.setUsername("lea@pop.com");
			p1.setPassword("senha@123");
			p1.setLink("amazon.com");
			p1.setNotes("Amazon website");
			p1.setPerson(p);

			pindao.insert(p1);

			Pin p2 = new Pin();
			p2.setTitle("Google");
			p2.setUsername("emma@pop.com");
			p2.setPassword("senha@123");
			p2.setLink("google.com");
			p2.setNotes("Google website");
			p2.setPerson(p);

			pindao.insert(p2);

			Pin p3 = new Pin();
			p3.setTitle("Oracle");
			p3.setUsername("olivia@pop.com");
			p3.setPassword("senha@123");
			p3.setLink("oracle.com");
			p3.setNotes("Oracle website");
			p3.setPerson(p);

			pindao.insert(p3);

			Pin p4 = new Pin();
			p4.setTitle("Micrsoft");
			p4.setUsername("olivia@pop.com");
			p4.setPassword("senha@123");
			p4.setLink("micrsoft.com");
			p4.setNotes("Microsoft website");
			p4.setPerson(p);

			pindao.insert(p4);

			Pin p5 = new Pin();
			p5.setTitle("Citrix");
			p5.setUsername("olivia@pop.com");
			p5.setPassword("senha@123");
			p5.setLink("citrix.com");
			p5.setNotes("Citrix website");
			p5.setPerson(p);

			pindao.insert(p5);

			Pin p6 = new Pin();
			p6.setTitle("Redhat");
			p6.setUsername("olivia@pop.com");
			p6.setPassword("senha@123");
			p6.setLink("redhat.com");
			p6.setNotes("Redhat website");
			p6.setPerson(p);

			pindao.insert(p6);

			Pin p7 = new Pin();
			p7.setTitle("Apple");
			p7.setUsername("olivia@pop.com");
			p7.setPassword("senha@123");
			p7.setLink("apple.com");
			p7.setNotes("Apple website");
			p7.setPerson(p);

			pindao.insert(p7);

			Pin p8 = new Pin();
			p8.setTitle("Spring Framework");
			p8.setUsername("olivia@pop.com");
			p8.setPassword("senha@123");
			p8.setLink("spring.com");
			p8.setNotes("Spring website");
			p8.setPerson(p);

			pindao.insert(p8);

			Pin p9 = new Pin();
			p9.setTitle("CompIT");
			p9.setUsername("olivia@pop.com");
			p9.setPassword("senha@123");
			p9.setLink("compit.com");
			p9.setNotes("CompIT website");
			p9.setPerson(p);

			pindao.insert(p9);

			Pin p10 = new Pin();
			p10.setTitle("Cisco");
			p10.setUsername("olivia@pop.com");
			p10.setPassword("senha@123");
			p10.setLink("cisco.com");
			p10.setNotes("Cisco website");
			p10.setPerson(p);

			pindao.insert(p10);
		} catch (PersistanceException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void addPerson() {
		try {
			
			Person p = new Person();
			p.setUsername("lea");
			p.setPassword("senha");
			p.setLogged(true);

			persondao.insert(p);
			
			p = new Person();
			p.setUsername("zoe");
			p.setPassword("senha");
			p.setLogged(true);
			
			persondao.insert(p);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
