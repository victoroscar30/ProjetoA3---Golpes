package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class InsertDemoData {
    private static final String[] URLS = {
            "http://www.banco-falso.com/login",
            "http://www.google.com",
            "http://www.site-suspeito.net",
            "http://www.portal.com",
            "http://www.seguro.org"
    };

    private static final String[] DOMINIOS = {
            "banco-falso.com",
            "google.com",
            "site-suspeito.net",
            "portal.com",
            "seguro.org"
    };

    private static final String[] CLASSIFICACOES = {
            "phishing",
            "segura",
            "suspeita",
            "desconhecida",
            "segura"
    };

    private static final String[] NOMES = {
            "Alice Silva", "Bruno Costa", "Carlos Lima", "Daniela Rocha", "Eduardo Melo",
            "Fernanda Alves", "Gustavo Santos", "Helena Oliveira", "Igor Pereira", "Juliana Castro",
            "Kaique Souza", "Larissa Ribeiro", "Marcos Ferreira", "Natália Gomes", "Otávio Martins",
            "Patrícia Lopes", "Rafael Barbosa", "Sara Cardoso", "Thiago Carvalho", "Vanessa Teixeira",
            "William Dias", "Yasmin Correia", "Zé Henrique Pinto", "Amanda Mendes", "Breno Nunes",
            "Cecília Reis", "Diego Moreira", "Elisa Batista", "Fábio Neves", "Gabriela Guimarães",
            "Hugo Andrade", "Isabela Tavares", "João Vitor Monteiro", "Karina Farias", "Leonardo Peixoto",
            "Mariana Vasconcelos", "Nicolas Ramos", "Olívia Brito", "Paulo César Azevedo", "Queila Fonseca",
            "Renato Machado", "Simone Dantas", "Tomás Barros", "Úrsula Freitas", "Vinícius Leal",
            "Wanessa Moura", "Xavier Bezerra", "Yago Siqueira", "Zara Figueiredo", "Adriano Veloso",
            "Bianca Cordeiro", "Caio Vidal", "Débora Miranda", "Elias Bento", "Flávia Caldas",
            "Gilberto Albuquerque", "Hadassa Câmara", "Ivan Rios", "Jéssica Aguiar", "Kléber Fontes",
            "Lorena Paiva", "Márcio Medeiros", "Noemi Araújo", "Orlando Quintanilha", "Priscila Bueno",
            "Quésia Santana", "Rogério Pires", "Sabrina Morais", "Tadeu Goulart", "Ubirajara Maia",
            "Valentina Falcão", "Wagner Salgado", "Ximena Assunção", "Yuri Canejo", "Zuleica Camargo",
            "Alexandre Bessa", "Bárbara Valença", "Cícero Domingues", "Dália Lessa", "Estevão Júnior",
            "Fátima Cortês", "Geraldo Coutinho", "Hortência Saldanha", "Ícaro Trindade", "Jacira Paranhos",
            "Kauê Torres", "Lúcio Villas", "Mirela Bandeira", "Nestor Ávila", "Ofélia Caetano",
            "Péricles Góis", "Quitéria Franco", "Raul Linhares", "Sueli Couto", "Tércio das Neves",
            "Uriel Telles", "Virgínia Ximenes", "Wilson Barreto", "Xisto Esteves", "Yolanda Lage",
            "Zacarias Sacramento", "Aline Maciel", "Bernardo Pimentel", "Cláudio Portela", "Diana Ávila",
            "Éder Padilha", "Franciele Tavares", "Gilda Varela", "Heitor Godói", "Inara Pinheiro",
            "Joaquim Abreu", "Kelly Vasques", "Lázaro Britto", "Márcia Paranhos", "Nara Góes",
            "Oswaldo Meireles", "Penha Bulhões", "Quintino Vargas", "Rosana Viveiros", "Sílvio Rosa",
            "Tatiana Pessoa", "Ubiratã Cerqueira", "Vanda Gentil", "Waldir Tápia", "Xênia Bacelar",
            "Yasako Ilha", "Zélia Castanho", "Artur Bernardes", "Berenice Ferraz", "Custódio Costa",
            "Dirce Moura", "Eurico Jardim", "Filomena Gentil", "Gervásio Taveira", "Hermínia Viana",
            "Iracema Sá", "Júlio César", "Kátia Passos", "Leandro Fagundes", "Madalena Chaves",
            "Nelson Lacerda", "Odete Ataíde", "Plínio Matos", "Quirino Ferro", "Raimunda Nonata",
            "Severino Leite", "Teresinha Carneiro", "Ulisses Vilela", "Vera Lago", "Wanderley Azevedo",
            "Xisto Galvão", "Yara Tibiriçá", "Zacarias Guedes", "Alcione Pacheco", "Bartira Negrão",
            "Célio Marques", "Doralice Carrara", "Enoque Pessoa", "Floripes Bicalho", "Godofredo Abrantes",
            "Hilária Carvalhais", "Ildefonso Lousada", "Jurema Peçanha", "Lino Goulart", "Marieta Nazaré",
            "Norberto Sacramento", "Ondina Vilarinho", "Plácido Carvalheiro", "Querubina Vilalobos",
            "Romão Taveira", "Salomé Varanda", "Tobias Carvalhosa", "Urbano Vilas", "Violeta Carvalhais",
            "Waldemiro Vilas", "Xavier Teles", "Yvone Quintela", "Zózimo Távora", "Amaro Sardinha",
            "Belmira Vilas", "Crispim Taveira", "Delfina Vilalobos", "Eugénio Carvalhoso", "Frutuoso Carvalheiro",
            "Graça Carvalhais", "Hermenegildo Taveira", "Isaura Carvalhosa", "Jacinto Carvalheiro", "Lurdes Carvalhais",
            "Máximo Carvalhoso", "Noémia Carvalheiro", "Olegário Carvalhais", "Pureza Carvalhosa", "Quintiliano Carvalheiro",
            "Rosenda Carvalhais", "Saturnino Carvalhoso", "Teodora Carvalheiro", "Ubaldo Carvalhais", "Venceslau Carvalhoso",
            "Xisto Carvalheiro", "Zacarias Carvalhais", "Adelaide Carvalhosa", "Balduíno Carvalheiro", "Clementina Carvalhais",
            "Dionísio Carvalhoso", "Eulália Carvalheiro", "Filipe Carvalhais", "Gertrudes Carvalhosa", "Horácio Carvalheiro",
            "Iara Carvalhais", "Júlio Carvalhoso", "Lina Carvalheiro", "Moisés Carvalhais", "Nadja Carvalhosa",
            "Orestes Carvalheiro", "Palmira Carvalhais", "Querubim Carvalhoso", "Rute Carvalheiro", "Simeão Carvalhais",
            "Tadeu Carvalhosa", "Ubirajara Carvalheiro", "Vanda Carvalhais", "Wanda Carvalhosa", "Xavier Carvalheiro",
            "Yara Carvalhais", "Zélia Carvalhosa", "Abílio Carvalheiro", "Branca Carvalhais", "Custódio Carvalhoso",
            "Dina Carvalheiro", "Eurico Carvalhais", "Fabrício Carvalhosa", "Gilda Carvalheiro", "Hélder Carvalhais",
            "Íris Carvalhosa", "Jorge Carvalheiro", "Lara Carvalhais", "Mauro Carvalhosa", "Nélia Carvalheiro",
            "Oscar Carvalhais", "Paula Carvalhosa", "Quitéria Carvalheiro", "Ramiro Carvalhais", "Sandra Carvalhosa",
            "Telmo Carvalheiro", "Ulisses Carvalhais", "Vera Carvalhosa", "Walter Carvalheiro", "Xénia Carvalhais",
            "Yago Carvalhosa", "Zilda Carvalheiro", "Alberto Carvalhais", "Beatriz Carvalhosa", "César Carvalheiro",
            "Diana Carvalhais", "Emanuel Carvalhosa", "Fátima Carvalheiro", "Gonçalo Carvalhais", "Helena Carvalhosa",
            "Ivo Carvalheiro", "Joana Carvalhais", "Kevin Carvalhosa", "Lia Carvalheiro", "Mário Carvalhais",
            "Nuno Carvalhosa", "Olga Carvalheiro", "Pedro Carvalhais", "Rita Carvalhosa", "Sérgio Carvalheiro",
            "Tânia Carvalhais", "Vítor Carvalhosa", "Waldir Carvalheiro", "Ximena Carvalhais", "Yasmin Carvalhosa",
            "Zacarias Carvalheiro", "Aida Carvalhais", "Benjamim Carvalhosa", "Carla Carvalheiro", "Dinis Carvalhais",
            "Elisa Carvalhosa", "Filipe Carvalheiro", "Glória Carvalhais", "Hugo Carvalhosa", "Inês Carvalheiro",
            "José Carvalhais", "Kelly Carvalhosa", "Luís Carvalheiro", "Mafalda Carvalhais", "Nelson Carvalhosa",
            "Otília Carvalheiro", "Paulo Carvalhais", "Queila Carvalhosa", "Rui Carvalheiro", "Sofia Carvalhais",
            "Tiago Carvalhosa", "Urbano Carvalheiro", "Vasco Carvalhais", "Wilson Carvalhosa", "Xavier Carvalheiro",
            "Yara Carvalhais", "Zé Carvalhosa", "Ana Carvalheiro", "Bruno Carvalhais", "Catarina Carvalhosa",
            "Duarte Carvalheiro", "Eva Carvalhais", "Francisco Carvalhosa", "Gabriela Carvalheiro", "Henrique Carvalhais",
            "Irina Carvalhosa", "João Carvalheiro", "Leonor Carvalhais", "Miguel Carvalhosa", "Nádia Carvalheiro",
            "Óscar Carvalhais", "Patrícia Carvalhosa", "Quim Carvalheiro", "Raquel Carvalhais", "Salvador Carvalhosa",
            "Teresa Carvalheiro", "Ulisses Carvalhais", "Violeta Carvalhosa", "William Carvalheiro", "Xénia Carvalhais",
            "Yuri Carvalhosa", "Zita Carvalheiro", "Alice Oliveira", "Bernardo Sousa", "Clara Mendes",
            "David Costa", "Ema Rodrigues", "Fábio Fernandes", "Gisela Martins", "Hélder Lopes",
            "Inácio Silva", "Joel Pereira", "Kyara Santos", "Luca Alves", "Mia Ribeiro",
            "Nuno Ferreira", "Olívia Gomes", "Pablo Carvalho", "Quintino Cardoso", "Rosa Teixeira",
            "Samuel Dias", "Tito Correia", "Úrsula Pinto", "Válter Mendes", "Wanda Nunes",
            "Xavier Reis", "Yolanda Moreira", "Zacarias Batista", "Ariana Neves", "Benjamim Guimarães",
            "Cristiana Andrade", "Dário Tavares", "Eunice Monteiro", "Frederico Farias", "Gorete Peixoto",
            "Humberto Vasconcelos", "Ivete Brito", "Júlio Azevedo", "Liliana Fonseca", "Márcio Machado",
            "Natacha Dantas", "Ovídio Barros", "Pérola Freitas", "Quirino Leal", "Rosa Moura",
            "Santiago Bezerra", "Tânia Siqueira", "Ubirajara Figueiredo", "Virgínia Veloso", "Wagner Cordeiro",
            "Xana Vidal", "Yasmine Miranda", "Zélia Bento", "Adélia Caldas", "Baltazar Albuquerque",
            "Cândida Câmara", "Délio Rios", "Eugénia Aguiar", "Feliciano Fontes", "Graciete Paiva",
            "Horácio Medeiros", "Irina Araújo", "Jorge Quintanilha", "Lídia Bueno", "Mauro Santana",
            "Nídia Pires", "Olegário Morais", "Porfírio Goulart", "Querubina Maia", "Romeu Falcão",
            "Sara Salgado", "Telo Assunção", "Ulisses Canejo", "Vera Camargo", "Waldir Bessa",
            "Xénia Valença", "Yara Domingues", "Zacarias Lessa", "Alzira Júnior", "Basílio Cortês",
            "Célia Coutinho", "Dionísio Saldanha", "Eugénio Trindade", "Fátima Paranhos", "Gualberto Torres",
            "Hélia Villas", "Ismael Bandeira", "Júlia Ávila", "Lauro Caetano", "Mafalda Góis",
            "Nádia Franco", "Orestes Linhares", "Piedade Couto", "Quirino das Neves", "Rosalina Telles",
            "Santiago Ximenes", "Telo Barreto", "Urbano Esteves", "Vanda Lage", "Wilson Sacramento",
            "Xisto Maciel", "Yolanda Pimentel", "Zuleica Portela", "Aida Ávila", "Baltasar Padilha",
            "Cristóvão Tavares", "Diana Varela", "Emanuel Godói", "Florbela Pinheiro", "Gonçalo Abreu",
            "Hélio Vasques", "Iara Britto", "Jacinto Paranhos", "Lídia Góes", "Mário Meireles",
            "Nair Bulhões", "Oscar Vargas", "Porfíria Viveiros", "Quintino Rosa", "Rosário Pessoa",
            "Sílvia Cerqueira", "Tadeu Gentil", "Ubirajara Tápia", "Vera Bacelar", "Waldir Ilha",
            "Xisto Castanho", "Yara Bernardes", "Zacarias Ferraz", "Adelaide Costa", "Benjamim Moura",
            "Cristiana Jardim", "Dinis Gentil", "Ema Taveira", "Filipe Viana", "Glória Sá",
            "Hélder César", "Inês Passos", "João Fagundes", "Kelly Chaves", "Luís Lacerda",
            "Mafalda Ataíde", "Nuno Matos", "Olívia Ferro", "Paulo Nonata", "Queila Leite",
            "Rui Carneiro", "Sofia Vilela", "Tiago Lago", "Urbano Azevedo", "Vasco Galvão",
            "Wilson Tibiriçá", "Xavier Guedes", "Yara Pacheco", "Zacarias Negrão", "Alice Marques",
            "Bruno Carrara", "Carlos Pessoa", "Daniela Bicalho", "Eduardo Abrantes", "Fernanda Carvalhais",
            "Gustavo Lousada", "Helena Peçanha", "Igor Goulart", "Juliana Nazaré", "Kaique Sacramento",
            "Larissa Vilarinho", "Marcos Carvalheiro", "Natália Vilalobos", "Otávio Taveira", "Patrícia Varanda",
            "Rafael Carvalhosa", "Sara Vilas", "Thiago Carvalhais", "Vanessa Carvalhosa", "William Carvalheiro",
            "Yasmin Carvalhais", "Zé Henrique Carvalhosa", "Amanda Carvalheiro", "Breno Carvalhais", "Cecília Carvalhosa",
            "Diego Carvalheiro", "Elisa Carvalhais", "Fábio Carvalhosa", "Gabriela Carvalheiro", "Hugo Carvalhais",
            "Isabela Carvalhosa", "João Vitor Carvalheiro", "Karina Carvalhais", "Leonardo Carvalhosa", "Mariana Carvalheiro",
            "Nicolas Carvalhais", "Olívia Carvalhosa", "Paulo César Carvalheiro", "Queila Carvalhais", "Renato Carvalhosa",
            "Simone Carvalheiro", "Tomás Carvalhais", "Úrsula Carvalhosa", "Vinícius Carvalheiro", "Wanessa Carvalhais",
            "Xavier Carvalhosa", "Yago Carvalheiro", "Zara Carvalhais", "Adriano Carvalhosa", "Bianca Carvalheiro",
            "Caio Carvalhais", "Débora Carvalhosa", "Elias Carvalheiro", "Flávia Carvalhais", "Gilberto Carvalhosa",
            "Hadassa Carvalheiro", "Ivan Carvalhais", "Jéssica Carvalhosa", "Kléber Carvalheiro", "Lorena Carvalhais",
            "Márcio Carvalhosa", "Noemi Carvalheiro", "Orlando Carvalhais", "Priscila Carvalhosa", "Quésia Carvalheiro",
            "Rogério Carvalhais", "Sabrina Carvalhosa", "Tadeu Carvalheiro", "Ubirajara Carvalhais", "Valentina Carvalhosa",
            "Wagner Carvalheiro", "Ximena Carvalhais", "Yuri Carvalhosa", "Zuleica Carvalheiro", "Alexandre Carvalhais",
            "Bárbara Carvalhosa", "Cícero Carvalheiro", "Dália Carvalhais", "Estevão Carvalhosa", "Fátima Carvalheiro",
            "Geraldo Carvalhais", "Hortência Carvalhosa", "Ícaro Carvalheiro", "Jacira Carvalhais", "Kauê Carvalhosa",
            "Lúcio Carvalheiro", "Mirela Carvalhais", "Nestor Carvalhosa", "Ofélia Carvalheiro", "Péricles Carvalhais",
            "Quitéria Carvalhosa", "Raul Carvalheiro", "Sueli Carvalhais", "Tércio Carvalhosa", "Uriel Carvalheiro",
            "Virgínia Carvalhais", "Wilson Carvalhosa", "Xisto Carvalheiro", "Yolanda Carvalhais", "Zacarias Carvalhosa",
            "Aline Carvalheiro", "Bernardo Carvalhais", "Cláudio Carvalhosa", "Diana Carvalheiro", "Éder Carvalhais",
            "Franciele Carvalhosa", "Gilda Carvalheiro", "Heitor Carvalhais", "Inara Carvalhosa", "Joaquim Carvalheiro",
            "Kelly Carvalhais", "Lázaro Carvalhosa", "Márcia Carvalheiro", "Nara Carvalhais", "Oswaldo Carvalhosa",
            "Penha Carvalheiro", "Quintino Carvalhais", "Rosana Carvalhosa", "Sílvio Carvalheiro", "Tatiana Carvalhais",
            "Ubiratã Carvalhosa", "Vanda Carvalheiro", "Waldir Carvalhais", "Xênia Carvalhosa", "Yasmin Carvalheiro",
            "Zélia Carvalhais", "Artur Carvalhosa", "Berenice Carvalheiro", "Custódio Carvalhais", "Dirce Carvalhosa",
            "Eurico Carvalheiro", "Filomena Carvalhais", "Gervásio Carvalhosa", "Hermínia Carvalheiro", "Iracema Carvalhais",
            "Júlio Carvalhosa", "Kátia Carvalheiro", "Leandro Carvalhais", "Madalena Carvalhosa", "Nelson Carvalheiro",
            "Odete Carvalhais", "Plínio Carvalhosa", "Quirino Carvalheiro", "Raimunda Carvalhais", "Severino Carvalhosa",
            "Teresinha Carvalheiro", "Ulisses Carvalhais", "Vera Carvalhosa", "Wanderley Carvalheiro", "Xisto Carvalhais",
            "Yara Carvalhosa", "Zacarias Carvalheiro", "Alcione Carvalhais", "Bartira Carvalhosa", "Célio Carvalheiro",
            "Doralice Carvalhais", "Enoque Carvalhosa", "Floripes Carvalheiro", "Godofredo Carvalhais", "Hilária Carvalhosa",
            "Ildefonso Carvalheiro", "Jurema Carvalhais", "Lino Carvalhosa", "Marieta Carvalheiro", "Norberto Carvalhais",
            "Ondina Carvalhosa", "Plácido Carvalheiro", "Querubina Carvalhais", "Romão Carvalhosa", "Salomé Carvalheiro",
            "Tobias Carvalhais", "Urbano Carvalhosa", "Violeta Carvalheiro", "Waldemiro Carvalhais", "Xavier Carvalhosa",
            "Yvone Carvalheiro", "Zózimo Carvalhais", "Amaro Carvalhosa", "Belmira Carvalheiro", "Crispim Carvalhais",
            "Delfina Carvalhosa", "Eugénio Carvalheiro", "Frutuoso Carvalhais", "Graça Carvalhosa", "Hermenegildo Carvalheiro",
            "Isaura Carvalhais", "Jacinto Carvalhosa", "Lurdes Carvalheiro", "Máximo Carvalhais", "Noémia Carvalhosa",
            "Olegário Carvalheiro", "Pureza Carvalhais", "Quintiliano Carvalhosa", "Rosenda Carvalheiro", "Saturnino Carvalhais",
            "Teodora Carvalhosa", "Ubaldo Carvalheiro", "Venceslau Carvalhais", "Xisto Carvalhosa", "Zacarias Carvalheiro",
            "Adelaide Carvalhais", "Balduíno Carvalhosa", "Clementina Carvalheiro", "Dionísio Carvalhais", "Eulália Carvalhosa",
            "Filipe Carvalheiro", "Gertrudes Carvalhais", "Horácio Carvalhosa", "Iara Carvalheiro", "Júlio Carvalhais",
            "Lina Carvalhosa", "Moisés Carvalheiro", "Nadja Carvalhais", "Orestes Carvalhosa", "Palmira Carvalheiro",
            "Querubim Carvalhais", "Rute Carvalhosa", "Simeão Carvalheiro", "Tadeu Carvalhais", "Ubirajara Carvalhosa",
            "Vanda Carvalheiro", "Wanda Carvalhais", "Xavier Carvalhosa", "Yara Carvalheiro", "Zélia Carvalhais"
    };

    private static final String[] EMAILS = {
            "alice@gmail.com", "bruno@hotmail.com", "carlos@yahoo.com", "daniela@gmail.com", "eduardo@gmail.com",
            "fernanda@hotmail.com", "gustavo@yahoo.com", "helena@gmail.com", "igor@hotmail.com", "juliana@yahoo.com",
            "kaique@gmail.com", "larissa@hotmail.com", "marcos@yahoo.com", "natalia@gmail.com", "otavio@hotmail.com",
            "patricia@yahoo.com", "rafael@gmail.com", "sara@hotmail.com", "thiago@yahoo.com", "vanessa@gmail.com",
            "william@hotmail.com", "yasmin@yahoo.com", "ze@gmail.com", "amanda@hotmail.com", "breno@yahoo.com",
            "cecilia@gmail.com", "diego@hotmail.com", "elisa@yahoo.com", "fabio@gmail.com", "gabriela@hotmail.com",
            "hugo@yahoo.com", "isabela@gmail.com", "joao@hotmail.com", "karina@yahoo.com", "leonardo@gmail.com",
            "mariana@hotmail.com", "nicolas@yahoo.com", "olivia@gmail.com", "paulo@hotmail.com", "queila@yahoo.com",
            "renato@gmail.com", "simone@hotmail.com", "tomas@yahoo.com", "ursula@gmail.com", "vinicius@hotmail.com",
            "wanessa@yahoo.com", "xavier@gmail.com", "yago@hotmail.com", "zara@yahoo.com", "adriano@gmail.com",
            "bianca@hotmail.com", "caio@yahoo.com", "debora@gmail.com", "elias@hotmail.com", "flavia@yahoo.com",
            "gilberto@gmail.com", "hadassa@hotmail.com", "ivan@yahoo.com", "jessica@gmail.com", "kleber@hotmail.com",
            "lorena@yahoo.com", "marcio@gmail.com", "noemi@hotmail.com", "orlando@yahoo.com", "priscila@gmail.com",
            "quesia@hotmail.com", "rogerio@yahoo.com", "sabrina@gmail.com", "tadeu@hotmail.com", "ubirajara@yahoo.com",
            "valentina@gmail.com", "wagner@hotmail.com", "ximena@yahoo.com", "yuri@gmail.com", "zuleica@hotmail.com",
            "alexandre@yahoo.com", "barbara@gmail.com", "cicero@hotmail.com", "dalia@yahoo.com", "estevao@gmail.com",
            "fatima@hotmail.com", "geraldo@yahoo.com", "hortencia@gmail.com", "icaro@hotmail.com", "jacira@yahoo.com",
            "kaue@gmail.com", "lucio@hotmail.com", "mirela@yahoo.com", "nestor@gmail.com", "ofelia@hotmail.com",
            "pericles@yahoo.com", "quiteria@gmail.com", "raul@hotmail.com", "sueli@yahoo.com", "tercio@gmail.com",
            "uriel@hotmail.com", "virginia@yahoo.com", "wilson@gmail.com", "xisto@hotmail.com", "yolanda@yahoo.com",
            "zacarias@gmail.com", "aline@hotmail.com", "bernardo@yahoo.com", "claudio@gmail.com", "diana@hotmail.com",
            "eder@yahoo.com", "franciele@gmail.com", "gilda@hotmail.com", "heitor@yahoo.com", "inara@gmail.com",
            "joaquim@hotmail.com", "kelly@yahoo.com", "lazaro@gmail.com", "marcia@hotmail.com", "nara@yahoo.com",
            "oswaldo@gmail.com", "penha@hotmail.com", "quintino@yahoo.com", "rosana@gmail.com", "silvio@hotmail.com",
            "tatiana@yahoo.com", "ubirata@gmail.com", "vanda@hotmail.com", "waldir@yahoo.com", "xenia@gmail.com",
            "yasmin@hotmail.com", "zelia@yahoo.com", "artur@gmail.com", "berenice@hotmail.com", "custodio@yahoo.com",
            "dirce@gmail.com", "eurico@hotmail.com", "filomena@yahoo.com", "gervasio@gmail.com", "herminia@hotmail.com",
            "iracema@yahoo.com", "julio@gmail.com", "katia@hotmail.com", "leandro@yahoo.com", "madalena@gmail.com",
            "nelson@hotmail.com", "odete@yahoo.com", "plinio@gmail.com", "quirino@hotmail.com", "raimunda@yahoo.com",
            "severino@gmail.com", "teresinha@hotmail.com", "ulisses@yahoo.com", "vera@gmail.com", "wanderley@hotmail.com",
            "xisto@yahoo.com", "yara@gmail.com", "zacarias@hotmail.com", "alcione@yahoo.com", "bartira@gmail.com",
            "celio@hotmail.com", "doralice@yahoo.com", "enoque@gmail.com", "floripes@hotmail.com", "godofredo@yahoo.com",
            "hilaria@gmail.com", "ildefonso@hotmail.com", "jurema@yahoo.com", "lino@gmail.com", "marieta@hotmail.com",
            "norberto@yahoo.com", "ondina@gmail.com", "placido@hotmail.com", "querubina@yahoo.com", "romao@gmail.com",
            "salome@hotmail.com", "tobias@yahoo.com", "urbano@gmail.com", "violeta@hotmail.com", "waldemiro@yahoo.com",
            "xavier@gmail.com", "yvone@hotmail.com", "zozimo@yahoo.com", "amaro@gmail.com", "belmira@hotmail.com",
            "crispim@yahoo.com", "delfina@gmail.com", "eugenio@hotmail.com", "frutuoso@yahoo.com", "graca@gmail.com",
            "hermenegildo@hotmail.com", "isaura@yahoo.com", "jacinto@gmail.com", "lurdes@hotmail.com", "maximo@yahoo.com",
            "noemia@gmail.com", "olegario@hotmail.com", "pureza@yahoo.com", "quintiliano@gmail.com", "rosenda@hotmail.com",
            "saturnino@yahoo.com", "teodora@gmail.com", "ubaldo@hotmail.com", "venceslau@yahoo.com", "xisto@gmail.com",
            "zacarias@hotmail.com", "adelaide@yahoo.com", "balduino@gmail.com", "clementina@hotmail.com", "dionisio@yahoo.com",
            "eulalia@gmail.com", "filipe@hotmail.com", "gertrudes@yahoo.com", "horacio@gmail.com", "iara@hotmail.com",
            "julio@yahoo.com", "lina@gmail.com", "moises@hotmail.com", "nadja@yahoo.com", "orestes@gmail.com",
            "palmira@hotmail.com", "querubim@yahoo.com", "rute@gmail.com", "simeao@hotmail.com", "tadeu@yahoo.com",
            "ubirajara@gmail.com", "vanda@hotmail.com", "wanda@yahoo.com", "xavier@gmail.com", "yara@hotmail.com",
            "zelia@yahoo.com", "abilio@gmail.com", "branca@hotmail.com", "custodio@yahoo.com", "dina@gmail.com",
            "eurico@hotmail.com", "fabricio@yahoo.com", "gilda@gmail.com", "helder@hotmail.com", "iris@yahoo.com",
            "jorge@gmail.com", "lara@hotmail.com", "mauro@yahoo.com", "nelia@gmail.com", "oscar@hotmail.com",
            "paula@yahoo.com", "quiteria@gmail.com", "ramiro@hotmail.com", "sandra@yahoo.com", "telmo@gmail.com",
            "ulisses@hotmail.com", "vera@yahoo.com", "walter@gmail.com", "xenia@hotmail.com", "yago@yahoo.com",
            "zilda@gmail.com", "alberto@hotmail.com", "beatriz@yahoo.com", "cesar@gmail.com", "diana@hotmail.com",
            "emanuel@yahoo.com", "fatima@gmail.com", "goncalo@hotmail.com", "helena@yahoo.com", "ivo@gmail.com",
            "joana@hotmail.com", "kevin@yahoo.com", "lia@gmail.com", "mario@hotmail.com", "nuno@yahoo.com",
            "olga@gmail.com", "pedro@hotmail.com", "rita@yahoo.com", "sergio@gmail.com", "tania@hotmail.com",
            "vitor@yahoo.com", "waldir@gmail.com", "ximena@hotmail.com", "yasmin@yahoo.com", "zacarias@gmail.com",
            "aida@hotmail.com", "benjamim@yahoo.com", "carla@gmail.com", "dinis@hotmail.com", "elisa@yahoo.com",
            "filipe@gmail.com", "gloria@hotmail.com", "hugo@yahoo.com", "ines@gmail.com", "jose@hotmail.com",
            "kelly@yahoo.com", "luis@gmail.com", "mafalda@hotmail.com", "nelson@yahoo.com", "otilia@gmail.com",
            "paulo@hotmail.com", "queila@yahoo.com", "rui@gmail.com", "sofia@hotmail.com", "tiago@yahoo.com",
            "urbano@gmail.com", "vasco@hotmail.com", "wilson@yahoo.com", "xavier@gmail.com", "yara@hotmail.com",
            "ze@yahoo.com", "ana@gmail.com", "bruno@hotmail.com", "catarina@yahoo.com", "duarte@gmail.com",
            "eva@hotmail.com", "francisco@yahoo.com", "gabriela@gmail.com", "henrique@hotmail.com", "irina@yahoo.com",
            "joao@gmail.com", "leonor@hotmail.com", "miguel@yahoo.com", "nadia@gmail.com", "oscar@hotmail.com",
            "patricia@yahoo.com", "quim@gmail.com", "raquel@hotmail.com", "salvador@yahoo.com", "teresa@gmail.com",
            "ulisses@hotmail.com", "violeta@yahoo.com", "william@gmail.com", "xenia@hotmail.com", "yuri@yahoo.com",
            "zita@gmail.com", "alice@hotmail.com", "bernardo@yahoo.com", "clara@gmail.com", "david@hotmail.com",
            "ema@yahoo.com", "fabio@gmail.com", "gisela@hotmail.com", "helder@yahoo.com", "inacio@gmail.com",
            "joel@hotmail.com", "kyara@yahoo.com", "luca@gmail.com", "mia@hotmail.com", "nuno@yahoo.com",
            "olivia@gmail.com", "pablo@hotmail.com","quintino@yahoo.com", "rosana@gmail.com", "silvio@hotmail.com", "tatiana@yahoo.com", "ubirata@gmail.com",
            "vanda@hotmail.com", "waldir@yahoo.com", "xenia@gmail.com", "yasmin@hotmail.com", "zelia@yahoo.com",
            "artur@gmail.com", "berenice@hotmail.com", "custodio@yahoo.com", "dirce@gmail.com", "eurico@hotmail.com",
            "filomena@yahoo.com", "gervasio@gmail.com", "herminia@hotmail.com", "iracema@yahoo.com", "julio@gmail.com",
            "katia@hotmail.com", "leandro@yahoo.com", "madalena@gmail.com", "nelson@hotmail.com", "odete@yahoo.com",
            "plinio@gmail.com", "quirino@hotmail.com", "raimunda@yahoo.com", "severino@gmail.com", "teresinha@hotmail.com",
            "ulisses@yahoo.com", "vera@gmail.com", "wanderley@hotmail.com", "xisto@yahoo.com", "yara@gmail.com",
            "zacarias@hotmail.com", "alcione@yahoo.com", "bartira@gmail.com", "celio@hotmail.com", "doralice@yahoo.com",
            "enoque@gmail.com", "floripes@hotmail.com", "godofredo@yahoo.com", "hilaria@gmail.com", "ildefonso@hotmail.com",
            "jurema@yahoo.com", "lino@gmail.com", "marieta@hotmail.com", "norberto@yahoo.com", "ondina@gmail.com",
            "placido@hotmail.com", "querubina@yahoo.com", "romao@gmail.com", "salome@hotmail.com", "tobias@yahoo.com",
            "urbano@gmail.com", "violeta@hotmail.com", "waldemiro@yahoo.com", "xavier@gmail.com", "yvone@hotmail.com",
            "zozimo@yahoo.com", "amaro@gmail.com", "belmira@hotmail.com", "crispim@yahoo.com", "delfina@gmail.com",
            "eugenio@hotmail.com", "frutuoso@yahoo.com", "graca@gmail.com", "hermenegildo@hotmail.com", "isaura@yahoo.com",
            "jacinto@gmail.com", "lurdes@hotmail.com", "maximo@yahoo.com", "noemia@gmail.com", "olegario@hotmail.com",
            "pureza@yahoo.com", "quintiliano@gmail.com", "rosenda@hotmail.com", "saturnino@yahoo.com", "teodora@gmail.com",
            "ubaldo@hotmail.com", "venceslau@yahoo.com", "xisto@gmail.com", "zacarias@hotmail.com", "adelaide@yahoo.com",
            "balduino@gmail.com", "clementina@hotmail.com", "dionisio@yahoo.com", "eulalia@gmail.com", "filipe@hotmail.com",
            "gertrudes@yahoo.com", "horacio@gmail.com", "iara@hotmail.com", "julio@yahoo.com", "lina@gmail.com",
            "moises@hotmail.com", "nadja@yahoo.com", "orestes@gmail.com", "palmira@hotmail.com", "querubim@yahoo.com",
            "rute@gmail.com", "simeao@hotmail.com", "tadeu@yahoo.com", "ubirajara@gmail.com", "vanda@hotmail.com",
            "wanda@yahoo.com", "xavier@gmail.com", "yara@hotmail.com", "zelia@yahoo.com", "abilio@gmail.com",
            "branca@hotmail.com", "custodio@yahoo.com", "dina@gmail.com", "eurico@hotmail.com", "fabricio@yahoo.com",
            "gilda@gmail.com", "helder@hotmail.com", "iris@yahoo.com", "jorge@gmail.com", "lara@hotmail.com",
            "mauro@yahoo.com", "nelia@gmail.com", "oscar@hotmail.com", "paula@yahoo.com", "quiteria@gmail.com",
            "ramiro@hotmail.com", "sandra@yahoo.com", "telmo@gmail.com", "ulisses@hotmail.com", "vera@yahoo.com",
            "walter@gmail.com", "xenia@hotmail.com", "yago@yahoo.com", "zilda@gmail.com", "alberto@hotmail.com",
            "beatriz@yahoo.com", "cesar@gmail.com", "diana@hotmail.com", "emanuel@yahoo.com", "fatima@gmail.com",
            "goncalo@hotmail.com", "helena@yahoo.com", "ivo@gmail.com", "joana@hotmail.com", "kevin@yahoo.com",
            "lia@gmail.com", "mario@hotmail.com", "nuno@yahoo.com", "olga@gmail.com", "pedro@hotmail.com",
            "rita@yahoo.com", "sergio@gmail.com", "tania@hotmail.com", "vitor@yahoo.com", "waldir@gmail.com",
            "ximena@hotmail.com", "yasmin@yahoo.com", "zacarias@gmail.com", "aida@hotmail.com", "benjamim@yahoo.com",
            "carla@gmail.com", "dinis@hotmail.com", "elisa@yahoo.com", "filipe@gmail.com", "gloria@hotmail.com",
            "hugo@yahoo.com", "ines@gmail.com", "jose@hotmail.com", "kelly@yahoo.com", "luis@gmail.com",
            "mafalda@hotmail.com", "nelson@yahoo.com", "otilia@gmail.com", "paulo@hotmail.com", "queila@yahoo.com",
            "rui@gmail.com", "sofia@hotmail.com", "tiago@yahoo.com", "urbano@gmail.com", "vasco@hotmail.com",
            "wilson@yahoo.com", "xavier@gmail.com", "yara@hotmail.com", "ze@yahoo.com", "ana@gmail.com",
            "bruno@hotmail.com", "catarina@yahoo.com", "duarte@gmail.com", "eva@hotmail.com", "francisco@yahoo.com",
            "gabriela@gmail.com", "henrique@hotmail.com", "irina@yahoo.com", "joao@gmail.com", "leonor@hotmail.com",
            "miguel@yahoo.com", "nadia@gmail.com", "oscar@hotmail.com", "patricia@yahoo.com", "quim@gmail.com",
            "raquel@hotmail.com", "salvador@yahoo.com", "teresa@gmail.com", "ulisses@hotmail.com", "violeta@yahoo.com",
            "william@gmail.com", "xenia@hotmail.com", "yuri@yahoo.com", "zita@gmail.com", "alice@hotmail.com",
            "bernardo@yahoo.com", "clara@gmail.com", "david@hotmail.com", "ema@yahoo.com", "fabio@gmail.com",
            "gisela@hotmail.com", "helder@yahoo.com", "inacio@gmail.com", "joel@hotmail.com", "kyara@yahoo.com",
            "luca@gmail.com", "mia@hotmail.com", "nuno@yahoo.com", "olivia@gmail.com", "pablo@hotmail.com",
            "quintino@yahoo.com", "rosana@gmail.com", "silvio@hotmail.com", "tatiana@yahoo.com", "ubirata@gmail.com",
            "vanda@hotmail.com", "waldir@yahoo.com", "xenia@gmail.com", "yasmin@hotmail.com", "zelia@yahoo.com",
            "artur@gmail.com", "berenice@hotmail.com", "custodio@yahoo.com", "dirce@gmail.com", "eurico@hotmail.com",
            "filomena@yahoo.com", "gervasio@gmail.com", "herminia@hotmail.com", "iracema@yahoo.com", "julio@gmail.com",
            "katia@hotmail.com", "leandro@yahoo.com", "madalena@gmail.com", "nelson@hotmail.com", "odete@yahoo.com",
            "plinio@gmail.com", "quirino@hotmail.com", "raimunda@yahoo.com", "severino@gmail.com", "teresinha@hotmail.com",
            "ulisses@yahoo.com", "vera@gmail.com", "wanderley@hotmail.com", "xisto@yahoo.com", "yara@gmail.com",
            "zacarias@hotmail.com", "alcione@yahoo.com", "bartira@gmail.com", "celio@hotmail.com", "doralice@yahoo.com",
            "enoque@gmail.com", "floripes@hotmail.com", "godofredo@yahoo.com", "hilaria@gmail.com", "ildefonso@hotmail.com",
            "jurema@yahoo.com", "lino@gmail.com", "marieta@hotmail.com", "norberto@yahoo.com", "ondina@gmail.com",
            "placido@hotmail.com", "querubina@yahoo.com", "romao@gmail.com", "salome@hotmail.com", "tobias@yahoo.com",
            "urbano@gmail.com", "violeta@hotmail.com", "waldemiro@yahoo.com", "xavier@gmail.com", "yvone@hotmail.com",
            "zozimo@yahoo.com", "amaro@gmail.com", "belmira@hotmail.com", "crispim@yahoo.com", "delfina@gmail.com",
            "eugenio@hotmail.com", "frutuoso@yahoo.com", "graca@gmail.com", "hermenegildo@hotmail.com", "isaura@yahoo.com",
            "jacinto@gmail.com", "lurdes@hotmail.com", "maximo@yahoo.com", "noemia@gmail.com", "olegario@hotmail.com",
            "pureza@yahoo.com", "quintiliano@gmail.com", "rosenda@hotmail.com", "saturnino@yahoo.com", "teodora@gmail.com",
            "ubaldo@hotmail.com", "venceslau@yahoo.com", "xisto@gmail.com", "zacarias@hotmail.com", "adelaide@yahoo.com",
            "balduino@gmail.com", "clementina@hotmail.com", "dionisio@yahoo.com", "eulalia@gmail.com", "filipe@hotmail.com",
            "gertrudes@yahoo.com", "horacio@gmail.com", "iara@hotmail.com", "julio@yahoo.com", "lina@gmail.com",
            "moises@hotmail.com", "nadja@yahoo.com", "orestes@gmail.com", "palmira@hotmail.com", "querubim@yahoo.com",
            "rute@gmail.com", "simeao@hotmail.com", "tadeu@yahoo.com", "ubirajara@gmail.com", "vanda@hotmail.com",
            "wanda@yahoo.com", "xavier@gmail.com", "yara@hotmail.com", "zelia@yahoo.com", "abilio@gmail.com",
            "branca@hotmail.com", "custodio@yahoo.com", "dina@gmail.com", "eurico@hotmail.com", "fabricio@yahoo.com",
            "gilda@gmail.com", "helder@hotmail.com", "iris@yahoo.com", "jorge@gmail.com", "lara@hotmail.com",
            "mauro@yahoo.com", "nelia@gmail.com", "oscar@hotmail.com", "paula@yahoo.com", "quiteria@gmail.com",
            "ramiro@hotmail.com", "sandra@yahoo.com", "telmo@gmail.com", "ulisses@hotmail.com", "vera@yahoo.com",
            "walter@gmail.com", "xenia@hotmail.com", "yago@yahoo.com", "zilda@gmail.com", "alberto@hotmail.com",
            "beatriz@yahoo.com", "cesar@gmail.com", "diana@hotmail.com", "emanuel@yahoo.com", "fatima@gmail.com",
            "goncalo@hotmail.com", "helena@yahoo.com", "ivo@gmail.com", "joana@hotmail.com", "kevin@yahoo.com",
            "lia@gmail.com", "mario@hotmail.com", "nuno@yahoo.com", "olga@gmail.com", "pedro@hotmail.com",
            "rita@yahoo.com", "sergio@gmail.com", "tania@hotmail.com", "vitor@yahoo.com", "waldir@gmail.com",
            "ximena@hotmail.com", "yasmin@yahoo.com", "zacarias@gmail.com", "aida@hotmail.com", "benjamim@yahoo.com",
            "carla@gmail.com", "dinis@hotmail.com", "elisa@yahoo.com", "filipe@gmail.com", "gloria@hotmail.com",
            "hugo@yahoo.com", "ines@gmail.com", "jose@hotmail.com", "kelly@yahoo.com", "luis@gmail.com",
            "mafalda@hotmail.com", "nelson@yahoo.com", "otilia@gmail.com", "paulo@hotmail.com", "queila@yahoo.com",
            "rui@gmail.com", "sofia@hotmail.com", "tiago@yahoo.com", "urbano@gmail.com", "vasco@hotmail.com",
            "wilson@yahoo.com", "xavier@gmail.com", "yara@hotmail.com", "ze@yahoo.com", "ana@gmail.com",
            "bruno@hotmail.com", "catarina@yahoo.com", "duarte@gmail.com", "eva@hotmail.com", "francisco@yahoo.com",
            "gabriela@gmail.com", "henrique@hotmail.com", "irina@yahoo.com", "joao@gmail.com", "leonor@hotmail.com",
            "miguel@yahoo.com", "nadia@gmail.com", "oscar@hotmail.com", "patricia@yahoo.com", "quim@gmail.com",
            "raquel@hotmail.com", "salvador@yahoo.com", "teresa@gmail.com", "ulisses@hotmail.com", "violeta@yahoo.com",
            "william@gmail.com", "xenia@hotmail.com", "yuri@yahoo.com", "zita@gmail.com"
    };

    private static final String[] GENEROS = {"f", "m"};
    private static final String[] TIPOS = {"comum"};

    private static final String[] MENSAGENS = {
            "Acesso a site classificado como phishing detectado.",
            "Site suspeito foi acessado.",
            "Outro acesso a site de phishing.",
            "Acesso repetido a URL suspeita.",
            "Phishing detectado novamente.",
            "Comportamento potencialmente malicioso."
    };

    public static void main(String[] args) {
        try (Connection conn = Conexao.conectar()) {
            inserirUrls(conn);
            inserirUsuarios(conn);
            inserirAcessos(conn);
            inserirAlertas(conn);
            System.out.println("Dados inseridos com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void inserirUrls(Connection conn) throws SQLException {
        String sql = "INSERT INTO urls (url, dominio, classificacao, data_cadastro) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            int idx = i % URLS.length;
            stmt.setString(1, URLS[idx] + "/id=" + i);
            stmt.setString(2, DOMINIOS[idx]);
            stmt.setString(3, CLASSIFICACOES[idx]);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().minusDays(rand.nextInt(365))));
            stmt.executeUpdate();
        }
    }

    private static void inserirUsuarios(Connection conn) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, genero, tipo, ip_origem, data_criacao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            String nome = NOMES[i % NOMES.length] + i;
            String email = EMAILS[i % EMAILS.length].replace("@", i + "@");
            String genero = GENEROS[i % GENEROS.length];
            String tipo = TIPOS[i % TIPOS.length];
            String ip = "192.168.0." + rand.nextInt(255);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, "senha123");
            stmt.setString(4, genero);
            stmt.setString(5, tipo);
            stmt.setString(6, ip);
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now().minusDays(rand.nextInt(365))));
            stmt.executeUpdate();
        }
    }

    private static void inserirAcessos(Connection conn) throws SQLException {
        String sql = "INSERT INTO acessos (id_usuario, id_url, data_acesso, suspeito) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            int id_usuario = rand.nextInt(10000) + 1;
            int id_url = rand.nextInt(10000) + 1;
            boolean suspeito = false;

            try (PreparedStatement urlStmt = conn.prepareStatement("SELECT classificacao FROM urls WHERE id = ?")) {
                urlStmt.setInt(1, id_url);
                try (ResultSet rs = urlStmt.executeQuery()) {
                    if (rs.next()) {
                        String classificacao = rs.getString("classificacao");
                        if ("phishing".equalsIgnoreCase(classificacao) || "suspeita".equalsIgnoreCase(classificacao)) {
                            suspeito = rand.nextDouble() < 0.9;
                        } else {
                            suspeito = rand.nextDouble() < 0.1;
                        }
                    }
                }
            }

            stmt.setInt(1, id_usuario);
            stmt.setInt(2, id_url);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(rand.nextInt(365))));
            stmt.setBoolean(4, suspeito);
            stmt.executeUpdate();
        }
    }

    private static void inserirAlertas(Connection conn) throws SQLException {
        String sql = "INSERT INTO alertas (id_acesso, mensagem, data_alerta) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        Random rand = new Random();
        for (int i = 1; i <= 10000; i++) {
            if (rand.nextDouble() < 0.6) { // Apenas 60% dos acessos recebem alerta
                stmt.setInt(1, i);
                stmt.setString(2, MENSAGENS[rand.nextInt(MENSAGENS.length)]);
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(rand.nextInt(365))));
                stmt.executeUpdate();
            }
        }
    }
}
