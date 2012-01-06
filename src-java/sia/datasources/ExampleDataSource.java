package sia.datasources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.models.Contact;
import sia.models.ContactAccount;
import sia.models.Protocol;
import sia.models.UserAccount;
import sia.utils.Dictionaries;


public class ExampleDataSource extends DataSource {
	/**
	 * Example Data source
	 * 
	 * Float's Mobile Agent (for Sony Ericsson)
	 */
	public ExampleDataSource() {
		extensions = new String[] {"*.*", "*.*", "*.*","*.*"};
		descriptions = new String[][] {new String[] {"File 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus porta aliquam porttitor. Suspendisse quis leo in libero vehicula vehicula. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec venenatis, dolor ut rhoncus eleifend, arcu quam ultrices enim, et venenatis massa tortor laoreet eros. Pellentesque nisl odio, mollis quis bibendum quis, varius vel turpis. Proin sed purus vel nisl venenatis feugiat. Nam sodales, sem vitae vulputate ornare, purus purus vulputate massa, in dictum ligula lorem sed elit."},
										new String[] {"File 2", "Vivamus dictum sodales justo quis tempus. Sed at nunc sed sapien pretium suscipit ac eget sem. Aliquam vulputate lorem sit amet massa facilisis semper. Nullam erat orci, sollicitudin eget imperdiet id, laoreet eu lorem. Duis sem massa, consectetur at sollicitudin ullamcorper, ornare id magna. Nulla vestibulum commodo orci, rutrum sodales justo lobortis in. Vestibulum tincidunt tempus enim vitae accumsan. Curabitur et ipsum risus, sit amet pharetra sapien. Pellentesque accumsan nisi diam. Donec dignissim, sem a sollicitudin porttitor, tortor turpis pretium enim, nec sagittis lacus risus et enim."},
										new String[] {"File 3", "Aenean eget augue ac ligula faucibus congue. Curabitur vitae lacus ipsum. Suspendisse molestie enim sed nisl interdum congue adipiscing elit varius. Nulla non placerat felis. Nunc diam sem, congue et sodales eleifend, viverra sit amet metus. Fusce aliquam tellus turpis, nec euismod enim. Nullam lacus tellus, posuere vitae ultricies nec, pretium a nulla. Pellentesque pretium semper feugiat. Aliquam nisi mi, condimentum eu pulvinar eu, convallis ac quam. Mauris vel urna et erat iaculis vehicula."},
										new String[] {"File 4", "Maecenas ullamcorper facilisis scelerisque. Nulla facilisi. Vestibulum fringilla laoreet mi, vel pretium ante commodo dignissim. Duis at augue sed diam interdum convallis. Curabitur consectetur fermentum neque, eu eleifend ligula egestas et. Maecenas bibendum, mi quis faucibus auctor, mauris dolor sollicitudin lectus, ut dictum nisi risus vitae risus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam malesuada aliquam ultrices."}};
		passwordDescriptions = new String[] {"login: ", "*pass: "};
		protocols = new HashMap<String, Protocol>();
		//parser = new ParserFactory("FmaParser").create();
	}

	@Override
	public List<UserAccount> getUserAccounts() {
		userAccounts = new ArrayList<UserAccount>();
		userAccounts.add(new UserAccount(0, new Protocol(0, "gg", "gg", "gg.png"), ""));
//		userAccounts.add(new UserAccount(1, new Protocol(1, "jabber", "jabb", "jabber.png"), "4053074@jabb.pl"));
//		userAccounts.add(new UserAccount(2, new Protocol(2, "gtalk", "google talk", "gtalk.png"), "4053074@gmail.com"));
//		userAccounts.add(new UserAccount(3, new Protocol(2, "gtalk", "google talk", "gtalk.png"), "4053074@google.com"));
		return userAccounts;
	}

	@Override
	public List<Contact> getContacts() {
		if(contacts==null) {
			Map<String, Protocol> protocols = Dictionaries.getInstance().getProtocols();
			
			contacts = new ArrayList<Contact>();
			contacts.add(new Contact(0, "AAA", "aaa", "ccc"));
			contacts.add(new Contact(1, "AAA1", "aaa1", "ccc1"));
			contacts.add(new Contact(0, "AAA2", "aaa2", "ccc2"));
			contacts.add(new Contact(0, "AAA3", "aaa3", "ccc3"));
			contacts.add(new Contact(0, "AAA4", "aaa4", "ccc4"));
			contacts.add(new Contact(0, "AAA5", "aaa5", "ccc5"));
			contacts.get(0).addContactAccount(new ContactAccount(0, "a1", "a1@a.pl", "aaaaaaa", contacts.get(0), protocols.get("SMS")));
			contacts.get(0).addContactAccount(new ContactAccount(1, "a2", "a2@a.pl", "aaaaaaa", contacts.get(0), new Protocol(0, "gg", "gg", "gg.png")));
			contacts.get(0).addContactAccount(new ContactAccount(2, "a3", "a3@a.pl", "aaaaaaa", contacts.get(0), protocols.get("SMS")));
			contacts.get(0).addContactAccount(new ContactAccount(3, "a4", "a4@a.pl", "aaaaaaa", contacts.get(0), new Protocol(1, "jabber", "jabb", "jabber.png")));
			
			contacts.get(1).addContactAccount(new ContactAccount(0, "b1", "b1@a.pl", "baaaaaaa", contacts.get(1), new Protocol(0, "gg", "gg", "gg.png")));
			contacts.get(1).addContactAccount(new ContactAccount(1, "b2", "b2@a.pl", "baaaaaaa", contacts.get(1), new Protocol(1, "jabber", "jabb", "jabber.png")));
			contacts.get(1).addContactAccount(new ContactAccount(0, "b3", "b3@a.pl", "baaaaaaa", contacts.get(1), new Protocol(2, "gtalk", "google talk", "gtalk.png")));
			contacts.get(1).addContactAccount(new ContactAccount(0, "b4", "b4@a.pl", "baaaaaaa", contacts.get(1), new Protocol(0, "gg", "gg", "gg.png")));
			
			contacts.get(2).addContactAccount(new ContactAccount(0, "ca1", "ca1@a.pl", "aaaaaaa", contacts.get(2), new Protocol(0, "gg", "gg", "gg.png")));
			contacts.get(2).addContactAccount(new ContactAccount(1, "ca2", "ca2@a.pl", "aaaaaaa", contacts.get(2), new Protocol(2, "gtalk", "google talk", "gtalk.png")));
			contacts.get(2).addContactAccount(new ContactAccount(0, "ca3", "ca3@a.pl", "aaaaaaa", contacts.get(2), new Protocol(2, "gtalk", "google talk", "gtalk.png")));
			contacts.get(2).addContactAccount(new ContactAccount(0, "ca4", "ca4@a.pl", "aaaaaaa", contacts.get(2), new Protocol(0, "gg", "gg", "gg.png")));
			
			contacts.get(3).addContactAccount(new ContactAccount(0, "da1", "da1@a.pl", "aaaaaaa", contacts.get(3), new Protocol(2, "gtalk", "google talk", "gtalk.png")));
			contacts.get(3).addContactAccount(new ContactAccount(1, "da2", "da2@a.pl", "aaaaaaa", contacts.get(3), new Protocol(0, "gg", "gg", "gg.png")));
			contacts.get(3).addContactAccount(new ContactAccount(0, "da3", "da3@a.pl", "aaaaaaa", contacts.get(3), new Protocol(2, "gtalk", "google talk", "gtalk.png")));
			contacts.get(3).addContactAccount(new ContactAccount(0, "da4", "da4@a.pl", "aaaaaaa", contacts.get(3), protocols.get("GG")));
			
			contacts.get(4).addContactAccount(new ContactAccount(0, "ea1", "ea1@a.pl", "aaaaaaa", contacts.get(4), new Protocol(0, "gg", "gg", "gg.png")));
			contacts.get(4).addContactAccount(new ContactAccount(0, "ea2", "ea2@a.pl", "aaaaaaa", contacts.get(4), new Protocol(2, "gtalk", "google talk", "gtalk.png")));
			contacts.get(4).addContactAccount(new ContactAccount(0, "ea3", "ea3@a.pl", "aaaaaaa", contacts.get(4), new Protocol(2, "gtalk", "google talk", "gtalk.png")));
			contacts.get(4).addContactAccount(new ContactAccount(0, "ea4", "ea4@a.pl", "aaaaaaa", contacts.get(4), new Protocol(0, "gg", "gg", "gg.png")));
			
			contacts.get(5).addContactAccount(new ContactAccount(0, "fa1", "fa1@a.pl", "aaaaaaa", contacts.get(5), new Protocol(0, "gg", "gg", "gg.png")));
			contacts.get(5).addContactAccount(new ContactAccount(0, "fa2", "fa2@a.pl", "aaaaaaa", contacts.get(5), new Protocol(1, "jabber", "jabb", "jabber.png")));
			contacts.get(5).addContactAccount(new ContactAccount(0, "fa3", "fa3@a.pl", "aaaaaaa", contacts.get(5), new Protocol(1, "jabber", "jabb", "jabber.png")));
			contacts.get(5).addContactAccount(new ContactAccount(0, "fa4", "fa4@a.pl", "aaaaaaa", contacts.get(5), new Protocol(0, "gg", "gg", "gg.png")));
		}
		
		return contacts;
	}

	@Override
	public void loadFiles(String[] files) {
		System.out.println("Load files");
	}
}
