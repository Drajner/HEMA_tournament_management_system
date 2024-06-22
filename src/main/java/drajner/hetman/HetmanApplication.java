package drajner.hetman;

import drajner.hetman.requests.Person;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HetmanApplication {

	@SneakyThrows
	public static void main(String[] args) {

		/*
		Tournament t1 = new Tournament("Turniej dupy");
		Tournament t2 = new Tournament("Drugi turniej dupy");
		Person p1 = new Person("jan", "pawel", "aaa");
		Person p2 = new Person("pan", "jawel", "aaa");
		Person p3 = new Person("pan", "pawel", "bbb");
		Person p4 = new Person("jan", "jawel", "bbb");
		Person p5 = new Person("juan", "pablito", "eee");
		Person p6 = new Person("pablo", "juanito", "eee");
		t1.addParticipant(p1);
		t1.addParticipant(p2);
		t1.addParticipant(p3);
		t1.addParticipant(p4);
		t1.addParticipant(p5);
		t1.addParticipant(p6);
		ReportsSingleton.add(t1);
		ReportsSingleton.add(t2);
		t1.createLadder(6);
		t1.getFinals().autoGenerateFights();
		 */
		SpringApplication.run(HetmanApplication.class, args);
	}

}
