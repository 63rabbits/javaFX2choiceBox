package javaFX2choiceBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChoiceBoxController {

	@FXML
	private ChoiceBox<String> chb;
	@FXML
	private Button btn;
	@FXML
	private TextField txf01;
	@FXML
	private TextField txf02;

	@FXML
	private ChoiceBox<String> chbHBind;
	@FXML
	private TextField txf01HBind;

	@FXML
	private ChoiceBox<String> chbLBind;
	@FXML
	private TextField txf01LBind;
	@FXML
	private TextField txf02LBind;

	private ArrayList<String> cocktailNames = new ArrayList<>();
	private HashMap<String, String> cocktailHmap = new HashMap<>();

	@FXML
	void initialize() {

		// get Cocktail list
		{
			URL url = this.getClass().getResource("res/cocktail.csv");
			OpCsv csv = new OpCsv(url);

			TreeMap<Integer, String[]> map = csv.getCsv();
			Iterator<Integer> it = map.keySet().iterator();
			while (it.hasNext()) {
				int no = it.next();
				String[] words = map.get(no);
				String ename = words[0];
				String jname = words[1];
				if (cocktailHmap.containsKey(ename)) {
					String duplicateKey = ename + " ## duplicate ##";
					cocktailHmap.put(duplicateKey, jname + " (T_T)");
				}
				else {
					cocktailHmap.put(ename, jname);
				}
			}

			Iterator<String> itCocktail = (new TreeSet<>(cocktailHmap.keySet())).iterator(); // sort the key
			while (itCocktail.hasNext()) {
				cocktailNames.add(itCocktail.next());
			}
		}

		// Using Event Handler
		assert chb != null : "fx:id=\"chb\" was not injected: check your FXML file 'CheckBox.fxml'.";
		assert btn != null : "fx:id=\"btn\" was not injected: check your FXML file 'CheckBox.fxml'.";
		assert txf01 != null : "fx:id=\"txf01\" was not injected: check your FXML file 'CheckBox.fxml'.";
		assert txf02 != null : "fx:id=\"txf02\" was not injected: check your FXML file 'CheckBox.fxml'.";
		this.chb.getItems().setAll(cocktailNames);
		this.chb.setUserData(cocktailHmap);
		this.chb.setValue(cocktailNames.get(0));
		this.txf01.setText(cocktailNames.get(0));
		@SuppressWarnings("unchecked")
		HashMap<String, String> chbHmap = (HashMap<String, String>) this.chb.getUserData();
		this.txf02.setText(chbHmap.get(this.chb.getValue()));
		this.btn.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("res/search.png"))));

		// Using Bind (High-leve API)
		assert chbHBind != null : "fx:id=\"chbHBind\" was not injected: check your FXML file 'CheckBox.fxml'.";
		assert txf01HBind != null : "fx:id=\"txf01HBind\" was not injected: check your FXML file 'CheckBox.fxml'.";
		this.chbHBind.getItems().setAll(cocktailNames);
		this.chbHBind.setValue(cocktailNames.get(0));
		this.txf01HBind.setText(cocktailNames.get(0));
		this.txf01HBind.textProperty().bind(this.chbHBind.valueProperty());

		// Using Bind (Low-leve API)
		assert chbLBind != null : "fx:id=\"chbLBind\" was not injected: check your FXML file 'CheckBox.fxml'.";
		assert txf01LBind != null : "fx:id=\"txf01LBind\" was not injected: check your FXML file 'CheckBox.fxml'.";
		assert txf02LBind != null : "fx:id=\"txf02LBind\" was not injected: check your FXML file 'CheckBox.fxml'.";
		this.chbLBind.getItems().setAll(cocktailNames);
		this.chbLBind.setValue(cocktailNames.get(0));
		this.chbLBind.setUserData(cocktailHmap);
		this.txf01LBind.setText(cocktailNames.get(0));
		this.txf01LBind.textProperty().bind(this.observer01(chbLBind));
		this.txf02LBind.textProperty().bind(this.observer02(chbLBind));
	}

	// Using Event Handler
	@FXML
	void btnOnAction(ActionEvent event) {
		this.txf01.setText(this.chb.getValue());
		@SuppressWarnings("unchecked")
		HashMap<String, String> hmap = (HashMap<String, String>) this.chb.getUserData();
		this.txf02.setText(hmap.get(this.chb.getValue()));
	}

	// Using Bind (High-leve API)

	// Using Bind (Low-leve API)
	private ObjectBinding<String> observer01(ChoiceBox<String> p) {
		final ChoiceBox<String> chb = p;
		ObjectBinding<String> sBinding = new ObjectBinding<String>() {
			{
				super.bind(chb.valueProperty());
			}

			@Override
			protected String computeValue() {
				return chb.getValue();
			}
		};
		return sBinding;
	}

	private ObjectBinding<String> observer02(ChoiceBox<String> p) {
		final ChoiceBox<String> cbh = p;
		ObjectBinding<String> sBinding = new ObjectBinding<String>() {
			{
				super.bind(cbh.valueProperty());
			}

			@Override
			protected String computeValue() {
				@SuppressWarnings("unchecked")
				HashMap<String, String> hmap = (HashMap<String, String>) cbh.getUserData();
				return hmap.get(cbh.getValue());
			}
		};
		return sBinding;
	}
}
