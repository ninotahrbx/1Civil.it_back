package fr.civilIteam.IncivilitiesTrack;


import fr.civilIteam.IncivilitiesTrack.models.*;
import fr.civilIteam.IncivilitiesTrack.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class IncivilitiesTrackApplication implements CommandLineRunner {

	private final ITypeRepository iTypeRepository;
	private final IStatusRepository iStatusRepository;
	private final IReportRepository iReportRepository;
	private final IRoleRepository iRoleRepository;
	private final IUserRepository iUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final IStatusRepository statusRepository;
	private final IGeolocationRepository geolocationRepository;
	private final ICommentRepository commentRepository;
	private final IReportRepository reportRepository;

	private final Date DATEOFDAY =new Date();


	private final IDocumentTypeRepository iDocumentTypeRepository;

	public static void main(String[] args) {

		SpringApplication.run(IncivilitiesTrackApplication.class, args);
	}

	private void initialiseData() {

//		List<Type> types = new ArrayList<>();
//		types.add(new Type(1L, UUID.randomUUID(), "Dépot sauvage d'ordures", new ArrayList<>()));
//		types.add(new Type(null, UUID.randomUUID(), "Vol", new ArrayList<>()));
//		iTypeRepository.saveAll(types);

		List<Role> roles = new ArrayList<>();
		Role userRole = new Role(null ,UUID.fromString("e9f9ffb9-0cfb-4448-8e58-9646ef29fac1"),"utilisateur",null );
		Role adminRole =new Role (null ,UUID.fromString("10aa86ee-301b-437c-bc18-20773926a4be"),"admin",null);
		Role citizenRole = new Role(null, null, "citoyen", new ArrayList<>());
		Role superAdminRole = new Role(null, null, "super-admin", new ArrayList<>());
		roles.add(userRole);
		roles.add(adminRole);
		roles.add(citizenRole);
		roles.add(superAdminRole);
		iRoleRepository.saveAll(roles);


		List<User> users = new ArrayList<>();
		User administrateur = new User(null,UUID.fromString("976a00db-d5df-426b-a1ee-702077f05550"),"admin@1civilit.com", passwordEncoder.encode("admin"), "admin","admin","0600000000",null,null,new Date(),null,null ,adminRole,null ,null,null,null,null);
		User utilisateur = new User(null,UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),"user@1civilit.com", passwordEncoder.encode("user"), "user","user","0600000000",null,null,new Date(),null,null ,userRole,null ,null,null,null,null);
		users.add(administrateur);
		users.add(utilisateur);
		User citizen = new User(
				null,
				UUID.fromString("976a00db-d5df-426b-a1ee-702077f05551"),
				"citizen@gmail.com",
				"Test@1234567",
				"toto",
				"Toto",
				"0422521010",
				null,
				null,
				new Date(),	// date de création
				null,	// date de connexion
				null,	// date de modification
				citizenRole,
				new ArrayList<>(),	// commentaires
				new ArrayList<>(),	// signalements
				new ArrayList<>(),	// identité
				new ArrayList<>(),	// messages envoyés
				new ArrayList<>()	// messages reçus
		);
		User admin = new User(
				null,
				UUID.fromString("976a00db-d5df-426b-a1ee-702077f05552"),
				"admin@gmail.com",
				"Test@1234567",
				"toto",
				"Toto",
				"0422521010",
				null,
				null,
				new Date(),	// date de création
				null,	// date de connexion
				null,	// date de modification
				roles.get(1),
				new ArrayList<>(),	// commentaires
				new ArrayList<>(),	// signalements
				new ArrayList<>(),	// identité
				new ArrayList<>(),	// messages envoyés
				new ArrayList<>()	// messages reçus
		);
		User superAdmin = new User(
				null,
				UUID.fromString("976a00db-d5df-426b-a1ee-702077f05553"),
				"super-admin@gmail.com",
				"Test@1234567",
				"toto",
				"Toto",
				"0422521010",
				null,
				null,
				new Date(),	// date de création
				null,	// date de connexion
				null,	// date de modification
				superAdminRole,
				new ArrayList<>(),	// commentaires
				new ArrayList<>(),	// signalements
				new ArrayList<>(),	// identité
				new ArrayList<>(),	// messages envoyés
				new ArrayList<>()	// messages reçus
		);
		users.add(citizen);
		users.add(admin);
		users.add(superAdmin);
		iUserRepository.saveAll(users);


		List<Type> types = new ArrayList<>();
		Type type1 = new Type();
		type1.setName("Dépot sauvage d'ordures");
		Type type2 = new Type();
		type2.setName("Agression");
		Type type3 = new Type();
		type3.setName("Vandalisme");
		Type type4 = new Type();
		type4.setName("Vol à l'arraché");
		Type type5 =new Type();
		type5.setName("Stationnement genant");
		types.add(type1);
		types.add(type2);
		types.add(type3);
		types.add(type4);
		types.add(type5);
		iTypeRepository.saveAll(types);

		List<Status>status = new ArrayList<>();

		Status status1 = new Status();
		status1.setName("Test Status");
		status.add(status1);
		Status status2 = new Status(null,UUID.fromString("55fcd3a7-c238-4f2f-87a6-bdea01ba7ebb"),"En cours",null);
		status.add(status2);
		Status status3=new Status(null,UUID.fromString("f98bbb0d-10e1-4a26-a16c-075f8f5b060c"),"Résolut",null);
		status.add(status3);
		Status status4 = new Status(null,UUID.fromString("3130fa86-91c6-430a-a114-afbef2a9f356"),"Rejeté",null);
		status.add(status4);
		statusRepository.saveAll(status);

		/*Report report1 = new Report(1L, UUID.randomUUID(),"imagePath","desc",new Date(),utilisateur,type1, status1,null,null,null);
		iReportRepository.save(report1);*/

		List<DocumentType> documentTypes = new ArrayList<>();
		DocumentType docType1 = new DocumentType();
		docType1.setName("Passeport");
		DocumentType docType2 = new DocumentType();
		docType2.setName("Carte d'identité recto");
		documentTypes.add(docType1);
		documentTypes.add(docType2);
		iDocumentTypeRepository.saveAll(documentTypes);


		List<Geolocation> geolocations =new ArrayList<>();
		Geolocation Geo1 = new Geolocation(null,UUID.randomUUID(),50.63, 3.066,null);
		Geolocation Geo2 = new Geolocation(null,UUID.randomUUID(),52.5,3.3,null);
		Geolocation Geo3 = new Geolocation(null,UUID.randomUUID(),55.5,4.3,null);
		Geolocation Geo4 = new Geolocation(null,UUID.randomUUID(),56.5,4.3236,null);
		Geolocation Geo5 = new Geolocation(null,UUID.randomUUID(),55.2,4.455,null);
		Geolocation Geo6 = new Geolocation(null,UUID.randomUUID(),55.4,4.2999,null);
		Geolocation Geo7 = new Geolocation(null,UUID.randomUUID(),55.6,4.3333,null);
		Geolocation Geo8 = new Geolocation(null,UUID.randomUUID(),55.9,4.4222,null);
		Geolocation Geo9 = new Geolocation(null,UUID.randomUUID(),55.01,4.35698,null);
		Geolocation Geo10 = new Geolocation(null,UUID.randomUUID(),56.01,4.55555,null);
		Geolocation Geo11 = new Geolocation(null,UUID.randomUUID(),52.01,4.11111,null);
		Geolocation Geo12 = new Geolocation(null,UUID.randomUUID(),55.666,4.29365,null);
		Geolocation Geo13 = new Geolocation(null,UUID.randomUUID(),54.01,4.88888,null);
		Geolocation Geo14 = new Geolocation(null,UUID.randomUUID(),50.666,4.3658,null);
		Geolocation Geo15 = new Geolocation(null,UUID.randomUUID(),50.3333,4.8955,null);
		Geolocation Geo16 = new Geolocation(null,UUID.randomUUID(),50.01,4.356668,null);
		Geolocation Geo17 = new Geolocation(null,UUID.randomUUID(),50.018965,4.35698,null);
		Geolocation Geo18 = new Geolocation(null,UUID.randomUUID(),50.1551,4.344448,null);
		geolocations.add(Geo1);
		geolocations.add(Geo2);
		geolocations.add(Geo3);
		geolocations.add(Geo4);
		geolocations.add(Geo5);
		geolocations.add(Geo6);
		geolocations.add(Geo7);
		geolocations.add(Geo8);
		geolocations.add(Geo9);
		geolocations.add(Geo10);
		geolocations.add(Geo11);
		geolocations.add(Geo12);
		geolocations.add(Geo13);
		geolocations.add(Geo14);
		geolocations.add(Geo15);
		geolocations.add(Geo16);
		geolocations.add(Geo17);
		geolocations.add(Geo18);
		geolocationRepository.saveAll(geolocations);

		List<Comment> comments = new ArrayList<>();
		Comment comment1 = new Comment(null,UUID.randomUUID(),new Date(),"test",null,null);
		Comment comment2 = new Comment(null,UUID.randomUUID(),new Date(),"test2",null,null);
		comments.add(comment1);
		comments.add(comment2);
		commentRepository.saveAll(comments);



		List<Report>reports = new ArrayList<>();
		reports.add(new Report(null,UUID.randomUUID(),"False img","test", DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-1),-2),utilisateur,type1,status.get(0),Geo1,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test2",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-2),-4),utilisateur,type1,status2,Geo2,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test3",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-3),-0),utilisateur,type1,status2,Geo3,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test4",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-4),-2),utilisateur,type1,status2,Geo4,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test5",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-2),-3),utilisateur,type1,status2,Geo5,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test6",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-0),-11),utilisateur,type2,status2,Geo6,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test7",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-4),-5),citizen,type2,status2,Geo7,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test8",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-0),-0),citizen,type2,status2,Geo8,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test9",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-0),-7),citizen,type2,status3,Geo9,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test10",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-2),-2),citizen,type2,status3,Geo10,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test11",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-3),-6),citizen,type2,status3,Geo11,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test12",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-5),-1),citizen,type2,status3,Geo12,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test13",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-2),-2),citizen,type3,status4,Geo13,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test14",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-1),-8),citizen,type3,status4,Geo14,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test15",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-1),-3),citizen,type3,status4,Geo15,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test16",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-0),-4),citizen,type4,status4,Geo16,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test17",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-2),-5),citizen,type4,status4,Geo15,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test18",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-7),-3),citizen,type4,status4,Geo16,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test19",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-2),-7),citizen,type5,status2,Geo12,null,null));
		reports.add(new Report(null,UUID.randomUUID(),"False img","test20",DateUtils.addHours (DateUtils.addDays( DATEOFDAY,-1),-1),citizen,type5,status2,Geo13,null,null));


		reportRepository.saveAll(reports);




	}

	@Override
	public void run(String... args) throws Exception {

		if (iTypeRepository.count() == 0 && iDocumentTypeRepository.count() == 0) {
			initialiseData();
		}

	}

}
