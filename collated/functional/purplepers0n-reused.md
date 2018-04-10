# purplepers0n-reused
###### \java\seedu\address\commons\events\ui\ChangeListTabEvent.java
``` java
/**
 * Indicates a request to change the tab to show other list
 */
public class ChangeListTabEvent extends BaseEvent {

    public final int targetList;

    public ChangeListTabEvent(int targetList) {
        this.targetList = targetList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\address\logic\parser\ListAllCommandParser.java
``` java
/**
 * Parses the input arguments and creates a new ListAllCommand object.
 */
public class ListAllCommandParser implements Parser<ListAllCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ListAllCommand
     * returns the specified object for execution.
     *
     * @throws ParseException if the user does not conform to expected format.
     */
    public ListAllCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ListAllCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAllCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\ui\ApptDisplayCard.java
``` java
/**
 * An UI component that displays information of an {@code Appointment} for the {@code ListAllPanel} display.
 */
public class ApptDisplayCard extends UiPart<Region> {

    private static final String FXML = "ApptDisplayCard.fxml";

    public final Appointment appointment;
    private String startTime;
    private String endTime;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label time;
    @FXML
    private Label petName;
    @FXML
    private Label vetTechName;
    @FXML
    private Label desc;

    public ApptDisplayCard(Appointment appointment, int startIndex) {
        super(FXML);

        this.appointment = appointment;
        id.setText(startIndex + ". ");
        startTime = appointment.getTime().toString();
        getTimeFrame(startTime, appointment.getDuration().toString());
        time.setText(startTime + " - " + endTime);
        petName.setText("Pet: " + appointment.getClientOwnPet().getPet().getPetName().fullPetName);
        if (appointment.getVetTechnician() == null) {
            vetTechName.setText("V.Tech: -");
        } else {
            vetTechName.setText("V.Tech: " + appointment.getVetTechnician().getName().fullName);
        }
        desc.setText("Description: " + appointment.getDescription().description);
        desc.setWrapText(true);
    }

    private void getTimeFrame(String time, String duration) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = df.parse(time);
            cal.setTime(date);
            cal.add(Calendar.MINUTE, Integer.parseInt(duration));
            endTime = df.format(cal.getTime());
        } catch (Exception e) {
            System.out.println("time cannot be parsed");
        }

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ApptDisplayCard)) {
            return false;
        }

        // state check
        ApptDisplayCard card = (ApptDisplayCard) other;
        return startTime.equals(card.startTime)
                && appointment.equals(card.appointment);
    }
}
```
###### \java\seedu\address\ui\ClientCard.java
``` java
/**
 * A UI component that displays information of a {@code Client}.
 */
public class ClientCard extends UiPart<Region> {

    private static final String FXML = "ClientListCard.fxml";
    private static final String[] TAG_COLOR = {"red", "yellow", "blue", "orange", "green",
        "pink", "navy", "teal", "purple", "peach", "lightblue",
        "green2", "white", "wine", "fuchsia", "sea"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Client client;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public ClientCard(Client client, int displayedIndex) {
        super(FXML);
        this.client = client;
        id.setText(displayedIndex + ". ");
        name.setText(client.getName().fullName);
        phone.setText(client.getPhone().value);
        address.setText(client.getAddress().value);
        email.setText(client.getEmail().value);
        initTags(client);
    }

    /**
     * @return the color for {@code tagName}'s label
     */
    private String getTagColorFor(String tagName) {
        return TAG_COLOR[Math.abs(tagName.hashCode()) % TAG_COLOR.length];
    }

    /**
     * Creates the tag labels for {@code client}.
     */
    private void initTags(Client client) {
        client.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClientCard)) {
            return false;
        }

        // state check
        ClientCard card = (ClientCard) other;
        return id.getText().equals(card.id.getText())
                && client.equals(card.client);
    }
}
```
###### \java\seedu\address\ui\ClientDisplayCard.java
``` java

/**
 * A UI component that displays information of a {@code Client} for {@code ListAllPanel} display.
 */
public class ClientDisplayCard extends UiPart<Region> {
    private static final String FXML = "ClientDisplayCard.fxml";

    private static final String[] TAG_COLOR = {"red", "yellow", "blue", "orange", "green",
        "pink", "navy", "teal", "purple", "peach", "lightblue", "darkpurple",
        "green2", "white", "wine", "fuchsia", "sea"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Client client;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public ClientDisplayCard(Client client) {
        super(FXML);
        this.client = client;
        name.setText(client.getName().fullName);
        phone.setText(client.getPhone().value);
        address.setText(client.getAddress().value);
        email.setText(client.getEmail().value);
        initTags(client);
    }

    /**
     * @return the color for {@code tagName}'s label
     */
    private String getTagColorFor(String tagName) {
        return TAG_COLOR[Math.abs(tagName.hashCode()) % TAG_COLOR.length];
    }

    /**
     * Creates the tag labels for {@code client}.
     */
    private void initTags(Client client) {
        client.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClientDisplayCard)) {
            return false;
        }

        // state check
        ClientDisplayCard card = (ClientDisplayCard) other;
        return client.equals(card.client);
    }
}
```
###### \java\seedu\address\ui\ClientListPanel.java
``` java
/**
 * Panel containing the list of clients.
 */
public class ClientListPanel extends UiPart<Region> {
    private static final String FXML = "ClientListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ClientListPanel.class);

    @FXML
    private ListView<ClientCard> clientListView;

    public ClientListPanel(ObservableList<Client> clientList) {
        super(FXML);
        setConnections(clientList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Client> clientList) {
        ObservableList<ClientCard> mappedList = EasyBind.map(
                clientList, (client) -> new ClientCard(client, clientList.indexOf(client) + 1));
        clientListView.setItems(mappedList);
        clientListView.setCellFactory(listView -> new ClientListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        clientListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in client list panel changed to : '" + newValue + "'");
                        raise(new ClientPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code ClientCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            clientListView.scrollTo(index);
            clientListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.targetList == 0) {
            scrollTo(event.targetIndex);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ClientCard}.
     */
    class ClientListViewCell extends ListCell<ClientCard> {

        @Override
        protected void updateItem(ClientCard client, boolean empty) {
            super.updateItem(client, empty);

            if (empty || client == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(client.getRoot());
            }
        }
    }

}
```
###### \java\seedu\address\ui\ListAllPanel.java
``` java
/**
 * Panel containing the list of client details.
 */
public class ListAllPanel extends UiPart<Region> {
    private static final String FXML = "ListAllPanel.fxml";

    @FXML
    private HBox listAllPane;
    @FXML
    private StackPane clientPane;
    @FXML
    private ListView<PetDisplayCard> petListView;
    @FXML
    private ListView<ApptDisplayCard> apptListView;

    public ListAllPanel(Client client, ObservableList<Pet> pets, ObservableList<Appointment> appts) {
        super(FXML);

        setClient(client);
        setPets(pets);
        setAppts(appts);
    }

    private void setClient(Client client) {
        ClientDisplayCard clientDisplayCard = new ClientDisplayCard(client);
        clientPane.getChildren().add(clientDisplayCard.getRoot());
    }

    private void setPets(ObservableList<Pet> pets) {
        ObservableList<PetDisplayCard> mappedList = EasyBind.map(pets, (pet) -> new PetDisplayCard(pet,
                pets.indexOf(pet) + 1));
        petListView.setItems(mappedList);
        petListView.setCellFactory(listView -> new PetListViewCell());
        petListView.setPrefHeight(pets.size() * 105);
    }

    private void setAppts(ObservableList<Appointment> appts) {
        ObservableList<ApptDisplayCard> mappedList = EasyBind.map(appts, (appt) -> new ApptDisplayCard(appt,
                appts.indexOf(appt) + 1));
        apptListView.setItems(mappedList);
        apptListView.setCellFactory(listView -> new ApptListViewCell());
        apptListView.setPrefHeight(appts.size() * 105);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PetDisplayCard}.
     */
    class PetListViewCell extends ListCell<PetDisplayCard> {

        @Override
        protected void updateItem(PetDisplayCard pet, boolean empty) {
            super.updateItem(pet, empty);

            if (empty || pet == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(pet.getRoot());
            }
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ApptDisplayCard}.
     */
    class ApptListViewCell extends ListCell<ApptDisplayCard> {

        @Override
        protected void updateItem(ApptDisplayCard appt, boolean empty) {
            super.updateItem(appt, empty);

            if (empty || appt == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(appt.getRoot());
            }
        }
    }
}
```
###### \java\seedu\address\ui\PetCard.java
``` java
/**
 * An UI component that displays information of a {@code clientOwnPet}.
 */
public class PetCard extends UiPart<Region> {

    private static final String FXML = "PetListCard.fxml";
    private static final String[] TAG_COLOR = {"red", "yellow", "blue", "orange", "green",
        "pink", "navy", "teal", "purple", "peach", "lightblue", "darkpurple",
        "green2", "white", "wine", "fuchsia", "sea"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ClientOwnPet clientOwnPet;
    public final Pet pet;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label gender;
    @FXML
    private Label age;
    @FXML
    private Label client;
    //species and breed included in tags
    @FXML
    private FlowPane tags;

    public PetCard(ClientOwnPet clientOwnPet, int displayedIndex) {
        super(FXML);
        this.clientOwnPet = clientOwnPet;
        pet = clientOwnPet.getPet();
        id.setText(displayedIndex + ". ");
        name.setText(pet.getPetName().fullPetName);
        gender.setText("Gender: " + pet.getPetGender().fullGender);
        age.setText(pet.getPetAge().value + " years old");
        client.setText("Owner: " + clientOwnPet.getClient().getName());
        initTags(pet);
    }

    /**
     * @return the color for {@code tagName}'s label
     */
    private String getTagColorFor(String tagName) {
        return TAG_COLOR[Math.abs(tagName.hashCode()) % TAG_COLOR.length];
    }

    /**
     * Creates the tag labels for {@code pet}.
     */
    private void initTags(Pet pet) {
        pet.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PetCard)) {
            return false;
        }

        // state check
        PetCard card = (PetCard) other;
        return id.getText().equals(card.id.getText())
                && pet.equals(card.pet);
    }
}
```
###### \java\seedu\address\ui\PetDisplayCard.java
``` java

/**
 * An UI component that displays information of a {@code clientOwnPet}.
 */
public class PetDisplayCard extends UiPart<Region> {

    private static final String FXML = "PetDisplayCard.fxml";
    private static final String[] TAG_COLOR = {"red", "yellow", "blue", "orange", "green",
        "pink", "navy", "teal", "purple", "peach", "lightblue", "darkpurple",
        "green2", "white", "wine", "fuchsia", "sea"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Pet pet;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label gender;
    @FXML
    private Label age;

    //species and breed included in tags
    @FXML
    private FlowPane tags;

    public PetDisplayCard(Pet pet, int displayedIndex) {
        super(FXML);
        this.pet = pet;
        id.setText(displayedIndex + ". ");
        name.setText(pet.getPetName().fullPetName);
        gender.setText("Gender: " + pet.getPetGender().fullGender);
        age.setText(pet.getPetAge().value + " years old");
        initTags(pet);
    }

    /**
     * @return the color for {@code tagName}'s label
     */
    private String getTagColorFor(String tagName) {
        return TAG_COLOR[Math.abs(tagName.hashCode()) % TAG_COLOR.length];
    }

    /**
     * Creates the tag labels for {@code pet}.
     */
    private void initTags(Pet pet) {
        pet.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PetDisplayCard)) {
            return false;
        }

        // state check
        PetDisplayCard card = (PetDisplayCard) other;
        return id.getText().equals(card.id.getText())
                && pet.equals(card.pet);
    }
}
```
###### \java\seedu\address\ui\PetListPanel.java
``` java
/**
 * Panel containing the list of pets.
 */
public class PetListPanel extends UiPart<Region> {
    private static final String FXML = "PetListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PetListPanel.class);

    @FXML
    private ListView<PetCard> petListView;

    public PetListPanel(ObservableList<ClientOwnPet> clientOwnPetList) {
        super(FXML);
        setConnections(clientOwnPetList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<ClientOwnPet> clientOwnPetList) {
        ObservableList<PetCard> mappedList = EasyBind.map(clientOwnPetList, (clientOwnPet) -> new PetCard(clientOwnPet,
                clientOwnPetList.indexOf(clientOwnPet) + 1));
        petListView.setItems(mappedList);
        petListView.setCellFactory(listView -> new PetListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        petListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in pet list panel changed to : '" + newValue + "'");
                        raise(new PetPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PetCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            petListView.scrollTo(index);
            petListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.targetList == 1) {
            scrollTo(event.targetIndex);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PetCard}.
     */
    class PetListViewCell extends ListCell<PetCard> {

        @Override
        protected void updateItem(PetCard pet, boolean empty) {
            super.updateItem(pet, empty);

            if (empty || pet == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(pet.getRoot());
            }
        }
    }

}
```
###### \java\seedu\address\ui\VetTechnicianCard.java
``` java
/**
 * An UI component that displays information of a {@code VetTechnician}.
 */
public class VetTechnicianCard extends UiPart<Region> {

    private static final String FXML = "VetTechnicianListCard.fxml";
    private static final String[] TAG_COLOR = {"red", "yellow", "blue", "orange", "green",
        "pink", "navy", "teal", "purple", "peach", "lightblue", "darkpurple",
        "green2", "white", "wine", "fuchsia", "sea"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final VetTechnician vetTechnician;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public VetTechnicianCard(VetTechnician vetTechnician, int displayedIndex) {
        super(FXML);
        this.vetTechnician = vetTechnician;
        id.setText(displayedIndex + ". ");
        name.setText(vetTechnician.getName().fullName);
        phone.setText(vetTechnician.getPhone().value);
        address.setText(vetTechnician.getAddress().value);
        email.setText(vetTechnician.getEmail().value);
        initTags(vetTechnician);
    }

    /**
     * @return the color for {@code tagName}'s label
     */
    private String getTagColorFor(String tagName) {
        return TAG_COLOR[Math.abs(tagName.hashCode()) % TAG_COLOR.length];
    }

    /**
     * Creates the tag labels for {@code client}.
     */
    private void initTags(VetTechnician vetTechnician) {
        vetTechnician.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VetTechnicianCard)) {
            return false;
        }

        // state check
        VetTechnicianCard card = (VetTechnicianCard) other;
        return id.getText().equals(card.id.getText())
                && vetTechnician.equals(card.vetTechnician);
    }
}
```
###### \java\seedu\address\ui\VetTechnicianListPanel.java
``` java
/**
 * Panel containing the list of vetTechnicians.
 */
public class VetTechnicianListPanel extends UiPart<Region> {
    private static final String FXML = "VetTechnicianListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(VetTechnicianListPanel.class);

    @FXML
    private ListView<VetTechnicianCard> vetTechnicianListView;

    public VetTechnicianListPanel(ObservableList<VetTechnician> vetTechnicianList) {
        super(FXML);
        setConnections(vetTechnicianList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<VetTechnician> vetTechnicianList) {
        ObservableList<VetTechnicianCard> mappedList = EasyBind.map(
                vetTechnicianList, (vetTechnician) ->
                        new VetTechnicianCard(vetTechnician, vetTechnicianList.indexOf(vetTechnician) + 1));
        vetTechnicianListView.setItems(mappedList);
        vetTechnicianListView.setCellFactory(listView -> new VetTechnicianListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        vetTechnicianListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in vetTechnician list panel changed to : '" + newValue + "'");
                        raise(new VetTechnicianPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code VetTechnicianCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            vetTechnicianListView.scrollTo(index);
            vetTechnicianListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.targetList == 2) {
            scrollTo(event.targetIndex);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code VetTechnicianCard}.
     */
    class VetTechnicianListViewCell extends ListCell<VetTechnicianCard> {

        @Override
        protected void updateItem(VetTechnicianCard vetTechnician, boolean empty) {
            super.updateItem(vetTechnician, empty);

            if (empty || vetTechnician == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(vetTechnician.getRoot());
            }
        }
    }

}
```
###### \resources\view\ApptDisplayCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15" />
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="time" text="\$time" styleClass="cell_big_label" />
            </HBox>
            <Label fx:id="petName" styleClass="cell_small_label" text="\$petName" />
            <Label fx:id="vetTechName" styleClass="cell_small_label" text="\$vetTechName" />
            <Label fx:id="desc" styleClass="cell_small_label" text="\$desc" />
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\ClientDisplayCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15" />
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="name" text="\$first" styleClass="cell_big_label" />
            </HBox>
            <FlowPane fx:id="tags" />
            <Label fx:id="phone" styleClass="cell_small_label" text="\$phone" />
            <Label fx:id="address" styleClass="cell_small_label" text="\$address" />
            <Label fx:id="email" styleClass="cell_small_label" text="\$email" />
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\ClientListCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15" />
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="name" text="\$first" styleClass="cell_big_label" />
            </HBox>
            <FlowPane fx:id="tags" />
            <Label fx:id="phone" styleClass="cell_small_label" text="\$phone" />
            <Label fx:id="address" styleClass="cell_small_label" text="\$address" />
            <Label fx:id="email" styleClass="cell_small_label" text="\$email" />
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\ClientListPanel.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <ListView fx:id="clientListView" prefWidth="248.0" VBox.vgrow="ALWAYS"/>
</VBox>
```
###### \resources\view\PetDisplayCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets bottom="5" left="15" right="5" top="5" />
            </padding>
            <HBox alignment="CENTER_LEFT" spacing="5">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="name" styleClass="cell_big_label" text="\$first" />
            </HBox>
            <FlowPane fx:id="tags" />
            <Label fx:id="gender" styleClass="cell_small_label" text="\$gender" />
            <Label fx:id="age" styleClass="cell_small_label" text="\$age" />
        </VBox>
        <rowConstraints>
            <RowConstraints />
        </rowConstraints>
    </GridPane>
</HBox>
```
###### \resources\view\PetListCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets bottom="5" left="15" right="5" top="5" />
            </padding>
            <HBox alignment="CENTER_LEFT" spacing="5">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="name" styleClass="cell_big_label" text="\$first" />
            </HBox>
            <FlowPane fx:id="tags" />
            <Label fx:id="gender" styleClass="cell_small_label" text="\$gender" />
            <Label fx:id="age" styleClass="cell_small_label" text="\$age" />
            <Label fx:id="client" styleClass="cell_small_label" text="\$client" />
        </VBox>
      <rowConstraints>
         <RowConstraints />
      </rowConstraints>
    </GridPane>
</HBox>
```
###### \resources\view\PetListPanel.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <ListView fx:id="petListView" prefWidth="248.0" VBox.vgrow="ALWAYS"/>
</VBox>
```
###### \resources\view\VetTechnicianListCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15" />
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="name" text="\$first" styleClass="cell_big_label" />
            </HBox>
            <FlowPane fx:id="tags" />
            <Label fx:id="phone" styleClass="cell_small_label" text="\$phone" />
            <Label fx:id="address" styleClass="cell_small_label" text="\$address" />
            <Label fx:id="email" styleClass="cell_small_label" text="\$email" />
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\VetTechnicianListPanel.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <ListView fx:id="vetTechnicianListView" prefWidth="248.0" VBox.vgrow="ALWAYS"/>
</VBox>
```
