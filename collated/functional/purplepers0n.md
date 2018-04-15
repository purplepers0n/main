# purplepers0n
###### \java\seedu\address\logic\commands\ListAllCommand.java
``` java
/**
 * Lists all details of a client, pet, technician or appointment in the address book.
 */
public class ListAllCommand extends Command {

    public static final String COMMAND_WORD = "listall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all associated details of the specified index of the specified type\n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Listed all details of client %1$s";

    private final Index targetIndex;
    private Client displayClient;
    private ObservableList<Pet> displayPet = FXCollections.observableArrayList();
    private ObservableList<Appointment> displayAppt = FXCollections.observableArrayList();

    public ListAllCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        if (targetIndex.getZeroBased() >= model.getFilteredClientList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        displayClient = model.getFilteredClientList().get(targetIndex.getZeroBased());
        setPets(displayClient);
        setAppts(displayPet);

        model.updateDetailsList(displayClient, displayPet, displayAppt);
        EventsCenter.getInstance().post(new NewListAllDisplayAvailableEvent(displayClient.getName().fullName));

        return new CommandResult(String.format(MESSAGE_SUCCESS, displayClient.getName().fullName));
    }

    private void setPets(Client client) {
        ObservableList<ClientOwnPet> clientOwnPets = model.getFilteredClientPetAssociationList();

        for (ClientOwnPet clientOwnPet : clientOwnPets) {
            if (clientOwnPet.getClient().equals(client)) {
                Pet currPet = clientOwnPet.getPet();
                displayPet.add(currPet);
            }
        }
    }

    private void setAppts(ObservableList<Pet> pets) {
        ObservableList<Appointment> appointmentList = model.getFilteredAppointmentList();

        for (Pet clientOwnPet : displayPet) {
            for (Appointment currAppt : appointmentList) {
                if (currAppt.getClientOwnPet() == null) {
                    continue;
                }
                if (currAppt.getClientOwnPet().getPet().equals(clientOwnPet)) {
                    displayAppt.add(currAppt);
                }
            }
        }
    }
}
```
###### \java\seedu\address\logic\commands\ListCommand.java
``` java
    @Override
    public CommandResult execute() throws CommandException {

        switch (targetType) {
        case "client":
            model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
            EventsCenter.getInstance().post(new ChangeListTabEvent(0));
            break;

        case "pet":
            model.updateFilteredClientOwnPetAssocation(PREDICATE_SHOW_ALL_ASSOCIATION);
            EventsCenter.getInstance().post(new ChangeListTabEvent(1));
            break;

        case "vettech":
            model.updateFilteredVetTechnicianList(PREDICATE_SHOW_ALL_TECHNICIAN);
            EventsCenter.getInstance().post(new ChangeListTabEvent(2));
            break;

        default:
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, targetType));
    }
}
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void updateDetailsList(Client client, ObservableList<Pet> pets,
                                  ObservableList<Appointment> appointments) {
        displayClient = client;
        displayPet = pets;
        displayAppt = appointments;
    }

    @Override
    public Client getClientDetails() {
        return displayClient;
    }

    @Override
    public ObservableList<Pet> getClientPetList() {
        return displayPet;
    }

    @Override
    public ObservableList<Appointment> getClientApptList() {
        return displayAppt;
    }

    /**
     * Clears the list all panel
     */
    private void clearListAllPanel() {
        displayClient = null;
        displayPet = null;
        displayAppt = null;
        indicateListAllPanelChanged();
    }

    /**
     * Updates the list all panel for UI
     */
    private void indicateListAllPanelChanged() {
        raise(new NewListAllDisplayAvailableEvent(null));
    }
}
```
###### \java\seedu\address\ui\ApptCard.java
``` java
/**
 * An UI component that displays information of an {@code Appointment}.
 */
public class ApptCard extends UiPart<Region> {

    private static final String FXML = "ApptCard.fxml";

    public final Appointment appointment;
    private String startTime;
    private String endTime;

    @FXML
    private HBox apptCardPane;
    @FXML
    private Label id;
    @FXML
    private Label time;
    @FXML
    private Label clientName;
    @FXML
    private Label petName;
    @FXML
    private Label vetTechName;

    public ApptCard(Appointment appointment, int startIndex) {
        super(FXML);

        this.appointment = appointment;
        id.setText(startIndex + ". ");
        startTime = appointment.getTime().toString();
        getTimeFrame(startTime, appointment.getDuration().toString());
        time.setText(startTime + " - " + endTime);
        if (appointment.getClientOwnPet() == null) {
            clientName.setText("Client: -");
            petName.setText("Pet: -");
        } else {
            clientName.setText("Client: " + appointment.getClientOwnPet().getClient().getName().fullName);
            petName.setText("Pet: " + appointment.getClientOwnPet().getPet().getPetName().fullPetName);
        }
        if (appointment.getVetTechnician() == null) {
            vetTechName.setText("V.Tech: -");
        } else {
            vetTechName.setText("V.Tech: " + appointment.getVetTechnician().getName().fullName);
        }
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
        if (!(other instanceof ApptCard)) {
            return false;
        }

        // state check
        ApptCard card = (ApptCard) other;
        return startTime.equals(card.startTime)
                && appointment.equals(card.appointment);
    }
}
```
###### \java\seedu\address\ui\ApptDayPanelCard.java
``` java
/**
 * Panel containing the list of appointments in a day
 */
public class ApptDayPanelCard extends UiPart<Region> {

    public static final String FXML = "ApptDayPanelCard.fxml";
    private static final String[] COLORS = {"red", "yellow", "blue", "orange", "green",
        "pink", "navy", "teal", "purple", "peach", "lightblue", "darkpurple",
        "green2", "wine", "fuchsia", "sea"};

    @FXML
    private ListView<ApptCard> apptDayListView;
    @FXML
    private Label dateDisplay;

    private String year;
    private String month;
    private String day;

    public ApptDayPanelCard(ObservableList<Appointment> apptDay, String date, int startIndex) {
        super(FXML);
        setConnections(apptDay, date, startIndex);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Appointment> apptDayList, String date, int startIndex) {
        ObservableList<ApptCard> mappedList = EasyBind.map(
                apptDayList, (appt) -> new ApptCard(appt, startIndex + apptDayList.indexOf(appt) + 1));
        apptDayListView.setItems(mappedList);
        apptDayListView.setCellFactory(listView -> new ApptListViewCell());

        year = date.substring(0, 4);
        day = date.substring(8, 10);

        int mon = Integer.parseInt(date.substring(5, 7));

        month = new DateFormatSymbols().getMonths()[mon - 1];

        dateDisplay.setText("  " + day + " " + month + " " + year);
        setColorFor(date);
    }

    /**
     * set the color for {@code date}'s label
     */
    private void setColorFor(String date) {

        String color = COLORS[Math.abs(date.hashCode()) % COLORS.length];
        dateDisplay.setId(color);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ApptCard}.
     */
    class ApptListViewCell extends ListCell<ApptCard> {

        @Override
        protected void updateItem(ApptCard appt, boolean empty) {
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
###### \java\seedu\address\ui\ApptListPanel.java
``` java
/**
 * Panel containing the list of all appointments
 */
public class ApptListPanel extends UiPart<Region> {

    private static final String FXML = "ApptListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ApptListPanel.class);

    @FXML
    private ListView<ApptDayPanelCard> apptListView;

    public ApptListPanel(ObservableList<Appointment> apptList) {
        super(FXML);
        setConnections(apptList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Appointment> apptList) {

        if (apptList.size() == 0) {
            return;
        }

        ObservableList<ApptDayPanelCard> mappedList = FXCollections.observableArrayList();
        Appointment lastAppt = null;
        int startIndex = 0;
        int endIndex;

        for (Appointment currAppt : apptList) {
            if (lastAppt != null && !currAppt.getDate().equals(lastAppt.getDate())) {
                endIndex = apptList.indexOf(currAppt);

                ObservableList<Appointment> apptDayList =
                        FXCollections.observableList(apptList.subList(startIndex, endIndex));
                mappedList.add(new ApptDayPanelCard(apptDayList, lastAppt.getDate().toString(), startIndex));

                startIndex = endIndex;
                lastAppt = currAppt;
            } else {
                lastAppt = currAppt;
            }
        }
        endIndex = apptList.size();
        ObservableList<Appointment> apptDayList = FXCollections.observableList(apptList.subList(startIndex, endIndex));
        mappedList.add(new ApptDayPanelCard(apptDayList, apptList.get(endIndex - 1).getDate().toString(), startIndex));

        apptListView.setItems(mappedList);
        apptListView.setCellFactory(listView -> new ApptListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ApptDayPanelCard}.
     */
    class ApptListViewCell extends ListCell<ApptDayPanelCard> {

        @Override
        protected void updateItem(ApptDayPanelCard appt, boolean empty) {
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
###### \java\seedu\address\ui\DateTimeCard.java
``` java
/**
 * Card displaying the current date and time
 */
public class DateTimeCard extends UiPart<Region> {

    private static final String FXML = "DateTimeCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label date;

    private int year;
    private String month;
    private int day;
    private int hour;
    private String minute;
    private boolean morning = true;

    public DateTimeCard() {
        super(FXML);
        setDateTime();
    }

    /**
     * Sets the current date and time
     */
    public void setDateTime() {

        final Timeline timeline = new Timeline(
                new KeyFrame(
                    Duration.millis(500),
                    event -> {
                        LocalDateTime now = LocalDateTime.now();

                        year = now.getYear();
                        Month currMonth = now.getMonth();
                        month = currMonth.toString();
                        day = now.getDayOfMonth();
                        if (now.getHour() >= 12) {
                            morning = false;
                        }
                        hour = now.getHour() % 12;
                        if (hour == 0) {
                            hour = 12;
                        }
                        int min = now.getMinute();

                        if (min < 10) {
                            minute = "0" + String.valueOf(min);
                        } else {
                            minute = String.valueOf(min);
                        }

                        if (morning) {
                            date.setText(hour + ":" + minute + " AM, " + day + " " + month + " " + year);
                        } else {
                            date.setText(hour + ":" + minute + " PM, " + day + " " + month + " " + year);
                        }
                    }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    void fillAppt() {
        apptListPanel = new ApptListPanel(logic.getFilteredAppointmentList());
        apptListPanelPlaceholder.getChildren().add(apptListPanel.getRoot());
    }

    /**
     * updates the listallpanel display
     */
    void fillListAllPanel() {
        if (logic.getClientDetails() != null) {
            listAllPanel = new ListAllPanel(logic.getClientDetails(),
                    logic.getClientPetList(), logic.getClientApptList());
            listAllPanelPlaceholder.getChildren().add(listAllPanel.getRoot());
        } else {
            listAllPanelPlaceholder.getChildren().remove(0, listAllPanelPlaceholder.getChildren().size());
        }
    }
```
###### \java\seedu\address\ui\MainWindow.java
``` java

    /**
     * Changes to the {@code Tab} of the specific {@code list} requested and selects it.
     */
    private void changeTo(int list) {
        Platform.runLater(() -> {
            listPanel.getSelectionModel().select(list);
        });
    }

    @Subscribe
    private void handleChangeListTabEvent(ChangeListTabEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        changeTo(event.targetList);
        logic.setCurrentList(event.targetList);
    }

    @Subscribe
    private void handleApptAvailableEvent(NewApptAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> fillAppt());
    }

    @Subscribe
    private void handleListAllDisplayAvailableEvent(NewListAllDisplayAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> fillListAllPanel());
    }

    /**
     * Updates the current index being viewed if tab is changed by mouseclick event
     */
    private void updateCurrentList() {
        listPanel.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                logic.setCurrentList(newValue.intValue());
            }
        });
    }
```
###### \resources\view\ApptCard.fxml
``` fxml
<HBox styleClass="apptCardPane" fx:id="apptCardPane" prefHeight="90" maxHeight="90" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="120" prefWidth="135" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="10" />
            </padding>
            <HBox alignment="CENTER_LEFT">
                <Label fx:id="id" id="cell_big_label_index">
                    <minWidth>
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="time" text="\$time" styleClass="cell_big_label" />
            </HBox>
            <Label fx:id="clientName" styleClass="cell_small_label" text="\$clientName" />
            <Label fx:id="petName" styleClass="cell_small_label" text="\$petName" />
            <Label fx:id="vetTechName" styleClass="cell_small_label" text="\$vetTechName" />
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\ApptDayPanelCard.fxml
``` fxml

<VBox xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <Label fx:id="dateDisplay" maxWidth="1.7976931348623157E308" minWidth="685" text="\$date" VBox.vgrow="ALWAYS" />
    <ListView fx:id="apptDayListView" orientation="HORIZONTAL" prefHeight="120" prefWidth="650" styleClass="apptDayPanel" VBox.vgrow="ALWAYS" />
</VBox>
```
###### \resources\view\ApptListPanel.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <ListView fx:id="apptListView" styleClass="apptList" VBox.vgrow="ALWAYS"/>
</VBox>
```
###### \resources\view\DarkTheme.css
``` css
.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.tab-pane .tab-header-area .tab-header-background {
    -fx-opacity: 0;
}

.tab {
    -fx-background-color: #515658;
}

.tab:selected {
    -fx-background-color: #3c3e3f;
    -fx-focus-color: transparent;
    -fx-faint-focus-color: transparent;
}

#tab-pane-list {
    -fx-font-size: 12pt;
    -fx-font-family: Helvetica;
    -fx-base: #141414;
    -fx-border-color: null;
}
```
###### \resources\view\DarkTheme.css
``` css
#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}

#tags .red {
    -fx-text-fill: #4F0810;
    -fx-background-color: #E57373;
}

#tags .yellow {
     -fx-text-fill: #542702;
     -fx-background-color: #FFF176;
}

#tags .blue {
    -fx-text-fill: #0E304C;
    -fx-background-color: #4D9DE0;
}

#tags .orange {
     -fx-text-fill: #381502;
     -fx-background-color: #F26419;
}

#tags .green {
     -fx-text-fill: #0F1E00;
     -fx-background-color: #97CC04;
}

#tags .pink {
     -fx-text-fill: #70112C;
     -fx-background-color: #F7628C;
}

#tags .navy {
     -fx-text-fill: #FFF8F0;
     -fx-background-color: #003366;
}

#tags .teal {
     -fx-text-fill: #082B2B;
     -fx-background-color: #3DBFBF;
}

#tags .purple {
     -fx-text-fill: #FFF8F0;
     -fx-background-color: #8C3678;
}

#tags .peach {
     -fx-text-fill: #3D2B21;
     -fx-background-color: #FFCF9C;
}

#tags .lightblue {
     -fx-text-fill: #10334C;
     -fx-background-color: #8EC8F2;
}

#tags .darkpurple {
     -fx-text-fill: #E2D3DD;
     -fx-background-color: #2B061E;
}

#tags .green2 {
     -fx-text-fill: #DEF7E8;
     -fx-background-color: #23CE6B;
}

#tags .white {
     -fx-text-fill: #222223;
     -fx-background-color: #F6F8FF;
}

#tags .wine {
     -fx-text-fill: #DDBABB;
     -fx-background-color: #400406;
}

#tags .fuchsia {
     -fx-text-fill: #440E3B;
     -fx-background-color: #C45AB3;
}

#tags .sea {
     -fx-text-fill: #CDF2F4;
     -fx-background-color: #105A5E;
}

#header-text {
    -fx-font-family: "coolvetica rg";
    -fx-fill: white;
    -fx-font-size: 35px;
}

.date-time-label {
    -fx-font-family: Helvetica;
    -fx-text-fill: white;
    -fx-font-size: 16px;
}

.apptCardPane {
     -fx-background-color: #3c3e3f;
     -fx-border-width: 0;
     -fx-padding: 2px;
     -fx-border-insets: 2px;
     -fx-background-insets: 2px;
}

#cell_big_label_index {
    -fx-font-family: Helvetica;
    -fx-font-size: 16px;
    -fx-text-fill: #DD3535;
}

.apptList .list-cell {
    -fx-background-color: transparent;
}

.apptDayPanel .list-cell {
    -fx-background-color: transparent;
}

#listalllabel {
    -fx-background-color: #6B7275;
    -fx-padding: 0px 5px 0px 10px
}
```
###### \resources\view\DateTimeCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="150" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="BOTTOM_RIGHT" minHeight="50" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="15" bottom="5" left="5" />
            </padding>
            <Label fx:id="date" styleClass="date-time-label" text="\$date" translateY="20"/>
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\ListAllPanel.fxml
``` fxml
<HBox fx:id="listAllPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <ListView>
        <StackPane fx:id="clientPane" />
        <StackPane>
            <Label id="listalllabel" text="Pets: " StackPane.alignment="CENTER_LEFT" minWidth="248"/>
        </StackPane>
        <HBox>
            <ListView fx:id="petListView" prefWidth="248.0" HBox.hgrow="ALWAYS"/>
        </HBox>
        <StackPane>
            <Label id="listalllabel" text="Appointments: " StackPane.alignment="CENTER_LEFT" minWidth="248"/>
        </StackPane>
        <HBox>
            <ListView fx:id="apptListView" prefWidth="248.0" HBox.hgrow="ALWAYS"/>
        </HBox>
    </ListView>
</HBox>
```
###### \resources\view\MainWindow.fxml
``` fxml
                <StackPane fx:id="stackPane" VBox.vgrow="NEVER" styleClass="pane-with-border" minHeight="75"
                           prefHeight="75"
                           maxHeight="75">
                    <ImageView fitHeight="70" fitWidth="70" pickOnBounds="true" preserveRatio="true" translateX="10"
                               StackPane.alignment="CENTER_LEFT">
                        <Image url="@/images/logo.png"/>
                    </ImageView>
                    <GridPane>
                        <Text id="header-text" text="VetterAppointments" translateX="85" translateY="15"/>
                    </GridPane>
                    <StackPane fx:id="dateTimePlaceholder" prefHeight="80"/>
                </StackPane>

                <SplitPane id="splitPane" fx:id="splitPane" dividerPositions="0.4" VBox.vgrow="ALWAYS">
                    <VBox fx:id="personList" minWidth="305" prefWidth="305" SplitPane.resizableWithParent="false">
                        <padding>
                            <Insets top="10" right="10" bottom="10" left="10"/>
                        </padding>
                        <TabPane id="tab-pane-list" fx:id="listPanel" prefWidth="248.0" tabClosingPolicy="UNAVAILABLE"
                                 VBox.vgrow="ALWAYS">
                            <tabs>
                                <Tab text="  Client  ">
                                    <content>
                                        <StackPane fx:id="clientListPanelPlaceholder" prefHeight="427.0"
                                                   prefWidth="248.0"/>
                                    </content>
                                </Tab>
                                <Tab text="    Pet    ">
                                    <content>
                                        <StackPane fx:id="petListPanelPlaceholder" prefHeight="427.0"
                                                   prefWidth="248.0"/>
                                    </content>
                                </Tab>
                                <Tab text=" Vet Tech ">
                                    <content>
                                        <StackPane fx:id="vetTechnicianListPanelPlaceholder" prefHeight="427.0"
                                                   prefWidth="248.0"/>
                                    </content>
                                </Tab>
                            </tabs>
                        </TabPane>
                    </VBox>

                    <VBox fx:id="placeHolder" prefWidth="340">
                        <StackPane VBox.vgrow="NEVER" fx:id="commandBoxPlaceholder" styleClass="anchor-pane"
                                   minHeight="50" prefHeight="50" maxHeight="50">
                            <padding>
                                <Insets top="5" right="5" bottom="5"/>
                            </padding>
                        </StackPane>
                        <StackPane VBox.vgrow="NEVER" fx:id="resultDisplayPlaceholder" styleClass="anchor-pane"
                                   minHeight="100" prefHeight="100" maxHeight="100">
                            <padding>
                                <Insets right="5" bottom="5"/>
                            </padding>
                        </StackPane>
                        <SplitPane id="splitPane" dividerPositions="0.8" VBox.vgrow="ALWAYS">
                            <VBox id="appt-display" >
                                <StackPane VBox.vgrow="ALWAYS" fx:id="apptListPanelPlaceholder" prefWidth="700"/>
                            </VBox>
                            <VBox id="details-display" minWidth="248" prefWidth="248" maxWidth="248">
                                <StackPane VBox.vgrow="ALWAYS" fx:id="listAllPanelPlaceholder" prefWidth="248"/>
                            </VBox>
                        </SplitPane>
                    </VBox>
                </SplitPane>
```
