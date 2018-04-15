# Godxin-functional
###### \java\seedu\address\logic\commands\RescheduleCommand.java
``` java
/**
 * Edits or reschedules the details of an existing appointment in the address book.
 */
public class RescheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "reschedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details, reschedules the time or deletes "
            + "the appointment identified "
            + "by the index number used in the appointment list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_DURATION + "DURATION] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2018-04-01 "
            + PREFIX_TIME + "16:00";

    public static final String MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS = "Successful !!!\n"
        + "  Appointment reschedules to : \n %1$s";
    public static final String MESSAGE_NOT_RESCHEDULED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the address book.";

    private final Index index;
    private final RescheduleAppointmentDescriptor rescheduleAppointmentDescriptor;

    private Appointment appointmentToReschedule;
    private Appointment rescheduledAppointment;

    /**
     * @param index                of the appointment in the filtered appointment list to edit(reschedule)
     * @param rescheduleAppointmentDescriptor details to reschedule the appointment with
     */
    public RescheduleCommand(Index index, RescheduleAppointmentDescriptor rescheduleAppointmentDescriptor) {
        requireNonNull(index);
        requireNonNull(rescheduleAppointmentDescriptor);

        this.index = index;
        this.rescheduleAppointmentDescriptor = new RescheduleAppointmentDescriptor(rescheduleAppointmentDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateAppointment(appointmentToReschedule, rescheduledAppointment);
        } catch (DuplicateAppointmentException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentNotFoundException anfe) {
            throw new AssertionError("The target appointment cannot be missing");
        }
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);

        return new CommandResult(String.format(MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS, rescheduledAppointment));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {

        List<Appointment> appointmentList = model.getFilteredAppointmentList();
        if (index.getZeroBased() >= appointmentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        appointmentToReschedule = appointmentList.get(index.getZeroBased());
        rescheduledAppointment = createRescheduledAppointment(appointmentToReschedule, rescheduleAppointmentDescriptor);
    }

    /**
     * Creates and returns a {@code Appointment} with the details of {@code appointmentToReschedule}
     * edited with {@code rescheduleAppointmentDescriptor}.
     */
    private static Appointment createRescheduledAppointment(
            Appointment appointmentToReschedule, RescheduleAppointmentDescriptor rescheduleAppointmentDescriptor) {
        assert appointmentToReschedule != null;

        Date updatedDate = rescheduleAppointmentDescriptor.getDate().orElse(appointmentToReschedule.getDate());
        Time updatedTime = rescheduleAppointmentDescriptor.getTime().orElse(appointmentToReschedule.getTime());
        Duration updatedDuration = rescheduleAppointmentDescriptor.getDuration().orElse(
                appointmentToReschedule.getDuration());
        Description updatedDescription = rescheduleAppointmentDescriptor.getDescription().orElse(
                appointmentToReschedule.getDescription());
        ClientOwnPet updatedClientOwnPet = appointmentToReschedule.getClientOwnPet();
        Optional<VetTechnician> updatedVetTech = appointmentToReschedule.getOptionalVetTechnician();

        Appointment newAppointment = new Appointment(updatedDate, updatedTime, updatedDuration, updatedDescription);
        newAppointment.setClientOwnPet(updatedClientOwnPet);
        newAppointment.setOptionalVetTech(updatedVetTech);
        return new Appointment(newAppointment);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RescheduleCommand)) {
            return false;
        }

        // state check
        RescheduleCommand r = (RescheduleCommand) other;
        return index.equals(r.index)
                && rescheduleAppointmentDescriptor.equals(r.rescheduleAppointmentDescriptor)
                && Objects.equals(appointmentToReschedule, r.appointmentToReschedule);
    }

    /**
     * Stores the details to reschedule the appointment with. Each non-empty field value will replace the
     * corresponding field value of the appointment.
     */
    public static class RescheduleAppointmentDescriptor {
        private Date date;
        private Time time;
        private Duration duration;
        private Description description;

        public RescheduleAppointmentDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public RescheduleAppointmentDescriptor(RescheduleAppointmentDescriptor toCopy) {
            setDate(toCopy.date);
            setTime(toCopy.time);
            setDuration(toCopy.duration);
            setDescription(toCopy.description);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.date, this.time, this.duration, this.description);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setTime(Time time) {
            this.time = time;
        }

        public Optional<Time> getTime() {
            return Optional.ofNullable(time);
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public Optional<Duration> getDuration() {
            return Optional.ofNullable(duration);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof RescheduleAppointmentDescriptor)) {
                return false;
            }

            // state check
            RescheduleAppointmentDescriptor r = (RescheduleAppointmentDescriptor) other;

            return getDate().equals(r.getDate())
                    && getTime().equals(r.getTime())
                    && getDuration().equals(r.getDuration())
                    && getDescription().equals(r.getDescription());
        }

    }
}
```
###### \java\seedu\address\logic\commands\ScheduleCommand.java
``` java
/**
 * Schedule the date and time for an appointment as well as the duration of this appointment
 */
public class ScheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "schedule";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Schedule an appointment with "
            + "date, time, duration and description.  "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_DURATION + "DURATION "
            + PREFIX_DESCRIPTION + "DESCRIPTION\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "2018-05-01 "
            + PREFIX_TIME + "15:15 "
            + PREFIX_DURATION + "60 "
            + PREFIX_DESCRIPTION + "Sterilize Garfield";


    public static final String MESSAGE_SUCCESS = "New appointment scheduled";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "The date and time are taken ";
    public static final String MESSAGE_CLOSE_APPOINTMENT_PREVIOUS = "The new appointment is within the duration"
            + " of the earlier appointment.\n";
    public static final String MESSAGE_SUGGESTION_TIME = "You may delay the appointment for: ";
    public static final String MESSAGE_CLOSE_APPOINTMENT_NEXT = "The later appointment is within the duration "
            + "of the new appointment.\n";
    public static final String MESSAGE_SUGGESTION_DURATION = "This appointment can last at most: ";
    private static final String MINUTE_SUFFIX = " minutes";
    private static final int MINIMUM_INTERVAL = 1440;
    private static final int CORRECT_DURATION = 120;
    private static final int CONVERSION_TIME = 60;
    private static final Time DEFAULT_TIME = new Time("23:59");

    private final Appointment toAdd;

    /**
     * Creates an ScheduleCommand to add the specified {@code appointment}
     */
    public ScheduleCommand(Appointment appointment) {
        requireNonNull(appointment);

        this.toAdd = appointment;
    }

    /**
     * Check that there is no earlier existing appointment too close
     */
    public void durationCheckPrevious(ObservableList<Appointment> existingAppointmentList)
            throws AppointmentCloseToPreviousException {

        Date newAppointmentDate = this.toAdd.getDate();
        Time newAppointmentTime = this.toAdd.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = MINIMUM_INTERVAL;
        int correctDuration = CORRECT_DURATION;

        for (Appointment earlierAppointment : existingAppointmentList) {
            Date earlierAppointmentDate = earlierAppointment.getDate();
            Time earlierAppointmentTime = earlierAppointment.getTime();

            if (newAppointmentDate.equals(earlierAppointmentDate)) {
                if (earlierAppointmentTime.getHour() < hour
                        || (earlierAppointmentTime.getHour() == hour && earlierAppointmentTime.getMinute() < min)) {
                    interval = (hour - earlierAppointmentTime.getHour()) * CONVERSION_TIME
                            + (min - earlierAppointmentTime.getMinute());
                    if (interval < minInterval) {
                        minInterval = interval;
                        correctDuration = earlierAppointment.getDuration().getDurationValue();
                    }
                }
            }
        }
        if (minInterval < correctDuration) {
            throw new AppointmentCloseToPreviousException(" Appointment is too close to earlier one");
        }
    }

    /**
     * Check that there is no later existing appointment too close
     */
    public void durationCheckNext(ObservableList<Appointment> existingAppointmentList)
            throws AppointmentCloseToNextException {

        Date newAppointmentDate = this.toAdd.getDate();
        Time newAppointmentTime = this.toAdd.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = this.toAdd.getDuration().getDurationValue();

        for (Appointment laterAppointment : existingAppointmentList) {
            Date laterAppointmentDate = laterAppointment.getDate();
            Time laterAppointmentTime = laterAppointment.getTime();

            if (newAppointmentDate.equals(laterAppointmentDate)) {
                if (laterAppointmentTime.getHour() > hour
                        || (laterAppointmentTime.getHour() == hour && min < laterAppointmentTime.getMinute())) {
                    interval = (laterAppointmentTime.getHour() - hour) * CONVERSION_TIME
                            + (laterAppointmentTime.getMinute() - min);
                    if (interval < minInterval) {
                        throw new AppointmentCloseToNextException(" Appointment is too close to later one");
                    }
                }
            }
        }
    }

    /**
     * Returns a duration
     */
    public Duration getSuggestedDelayDuration(ObservableList<Appointment> existingAppointmentList,
                                              Appointment appointment) {
        Date newAppointmentDate = appointment.getDate();
        Time newAppointmentTime = appointment.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = MINIMUM_INTERVAL;
        int correctDuration = CORRECT_DURATION;

        Time previous = DEFAULT_TIME;

        for (Appointment earlierAppointment : existingAppointmentList) {
            Date earlierAppointmentDate = earlierAppointment.getDate();
            Time earlierAppointmentTime = earlierAppointment.getTime();

            if (newAppointmentDate.equals(earlierAppointmentDate)) {
                if (earlierAppointmentTime.getHour() < hour
                        || (earlierAppointmentTime.getHour() == hour && earlierAppointmentTime.getMinute() < min)) {
                    interval = (hour - earlierAppointmentTime.getHour()) * CONVERSION_TIME
                            + (min - earlierAppointmentTime.getMinute());
                    if (interval < minInterval) {
                        minInterval = interval;
                        correctDuration = earlierAppointment.getDuration().getDurationValue() - minInterval;
                        previous = earlierAppointmentTime;
                    }
                }
            }
        }

        return new Duration(correctDuration);
    }

    /**
     * Returns a duration.
     */
    public Duration getSuggestedMaxDuration(ObservableList<Appointment> existingAppointmentList,
                                            Appointment appointment) {
        Date newAppointmentDate = appointment.getDate();
        Time newAppointmentTime = appointment.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = appointment.getDuration().getDurationValue();

        for (Appointment laterAppointment : existingAppointmentList) {
            Date laterAppointmentDate = laterAppointment.getDate();
            Time laterAppointmentTime = laterAppointment.getTime();

            if (newAppointmentDate.equals(laterAppointmentDate)) {
                if (laterAppointmentTime.getHour() > hour
                        || (laterAppointmentTime.getHour() == hour && min < laterAppointmentTime.getMinute())) {
                    interval = (laterAppointmentTime.getHour() - hour) * CONVERSION_TIME
                            + (laterAppointmentTime.getMinute() - min);
                    if (interval < minInterval) {
                        minInterval = interval;
                    }
                }
            }
        }
        return new Duration(minInterval);
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            durationCheckPrevious(model.getFilteredAppointmentList());
            durationCheckNext(model.getFilteredAppointmentList());
            model.scheduleAppointment(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateAppointmentException e1) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentCloseToPreviousException e2) {
            Duration suggestedDelayDuration = getSuggestedDelayDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_PREVIOUS + MESSAGE_SUGGESTION_TIME
                    + suggestedDelayDuration.toString() + MINUTE_SUFFIX);
        } catch (AppointmentCloseToNextException e3) {
            Duration suggestedMaxDuration = getSuggestedMaxDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_NEXT + MESSAGE_SUGGESTION_DURATION
                    + suggestedMaxDuration.toString() + MINUTE_SUFFIX);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleCommand // instanceof handles nulls
                && toAdd.equals(((ScheduleCommand) other).toAdd));
    }

}
```
###### \java\seedu\address\model\appointment\Appointment.java
``` java
/**
 * Represents an Appointment in the application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Appointment {

    private final Date date;
    private final Time time;
    private final Duration duration;
    private final Description description;
    private ClientOwnPet clientOwnPet;
    private Optional<VetTechnician> vetTech;

    /**
     * Every field must be present and not null.
     */
    public Appointment(Date date, Time time, Duration duration, Description description) {
        requireAllNonNull(date, time, duration, description);
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.description = description;
        clientOwnPet = null;
        vetTech = Optional.empty();
    }

    public Appointment(Appointment toCopy) {
        date = toCopy.getDate();
        time = toCopy.getTime();
        duration = toCopy.getDuration();
        description = toCopy.getDescription();
        clientOwnPet = toCopy.getClientOwnPet();
        vetTech = toCopy.getOptionalVetTechnician();
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Duration getDuration() {
        return duration;
    }

    public Description getDescription() {
        return description;
    }

    public ClientOwnPet getClientOwnPet() {
        return clientOwnPet;
    }

    public Optional<VetTechnician> getOptionalVetTechnician() {
        return vetTech;
    }
    public VetTechnician getVetTechnician() {
        return vetTech.orElse(null);
    }


    public void setClientOwnPet(ClientOwnPet clientOwnPet) {
        this.clientOwnPet = clientOwnPet;
    }

    public void setClientOwnPetToNull() {
        clientOwnPet = null;
    }

    public void setVetTech(VetTechnician vetTech) {
        this.vetTech = Optional.of(vetTech);
    }

    public void setOptionalVetTech (Optional<VetTechnician> vetTech) {
        this.vetTech = vetTech;
    }

    public void removeVetTech() {
        vetTech = Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return otherAppointment.getDate().equals(this.getDate())
                && otherAppointment.getTime().equals(this.getTime())
                && otherAppointment.getOptionalVetTechnician().equals(this.getOptionalVetTechnician());

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date, time, duration, description);

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(" Date: ")
                .append(getDate())
                .append(" Time: ")
                .append(getTime())
                .append(" Duration: ")
                .append(getDuration())
                .append(" Description: ")
                .append(getDescription());

        return builder.toString();
    }

    /**
     * Comparator that compares the date and then time of the appointment
     */
    public int compareTo(Appointment other) {
        if (this.getDate().compareToDate(other.getDate()) == 0) {
            return (this.getTime().compareToTime(other.getTime()));
        } else {
            return this.getDate().compareToDate(other.getDate());
        }
    }

}
```
###### \java\seedu\address\model\appointment\Date.java
``` java
/**
 * Represents an Appointment's date in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Appointment date should be all integers in format YYYY-MM-DD, and it should not be blank";
    public static final String MESSAGE_YEAR_CONSTRAINTS =
            "Appointment year should be later than 2018";
    public static final String MESSAGE_DAYINMONTH_CONSTRAINTS =
            "Appointment day does not exist in the month";
    /*
     * The first character of the date must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DATE_VALIDATION_REGEX =
            "([2-9][0-9][1-9][0-9])-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
    private static final int YEAR_START_INDEX = 0;
    private static final int YEAR_END_INDEX = 4;
    private static final int YEAR_LOWER_BOUND = 2018;
    private static final int LEAP_YEAR_DIVIDER = 4;
    private static final int CENTURY_YEAR_DIVIDER = 100;
    private static final int CENTURY_LEAP_YEAR_DIVIDER = 400;
    private static final int LEAP_YEAR_REMAINDER = 0;
    private static final int MONTH_START_INDEX = 5;
    private static final int MONTH_END_INDEX = 7;
    private static final int DAY_START_INDEX = 8;
    private static final int BIG_MONTH_DAY = 31;
    private static final int SMALL_MONTH_DAY = 30;
    private static final int FEB_LEAP_YEAR_DAY = 29;
    private static final int FEB_NONLEAP_YEAR_DAY = 28;
    private  static final String[] BIG_MONTH = {"01", "03", "05", "07", "08", "10", "12"};
    private  static final String[] SMALL_MONTH = {"04", "06", "09", "11"};


    public final String date;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        checkArgument(isValidYear(getYear(date)), MESSAGE_YEAR_CONSTRAINTS);
        checkArgument(isValidDaysInMonth(date), MESSAGE_DAYINMONTH_CONSTRAINTS);
        this.date = date;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidYear(int test) {
        return test >= YEAR_LOWER_BOUND;
    }

    /**
     * Returns true if a given string is a leap date.
     */
    public static boolean isLeapYear(int test) {
        if ((test % CENTURY_LEAP_YEAR_DIVIDER == LEAP_YEAR_REMAINDER)
                || ((test % LEAP_YEAR_DIVIDER == LEAP_YEAR_REMAINDER)
                && (test % CENTURY_YEAR_DIVIDER != LEAP_YEAR_REMAINDER))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if a given string is a valid date in month.
     */
    public static Boolean isValidDaysInMonth(String test) {
        int testYear = getYear(test);
        String testMonth = getMonth(test);
        int testDay = getDay(test);
        int daysInMonth;

        if (Arrays.asList(BIG_MONTH).contains(testMonth)) {
            daysInMonth = BIG_MONTH_DAY;
        } else if (Arrays.asList(SMALL_MONTH).contains(testMonth)) {
            daysInMonth = SMALL_MONTH_DAY;
        } else {
            if (isLeapYear(testYear)) {
                daysInMonth = FEB_LEAP_YEAR_DAY;
            } else {
                daysInMonth = FEB_NONLEAP_YEAR_DAY;
            }
        }
        return testDay <= daysInMonth;
    }

    /**
     *  Returns the integer value of year
     */
    public static int getYear(String date) {

        String year = date.substring(YEAR_START_INDEX, YEAR_END_INDEX);

        return Integer.parseInt(year);

    }

    /**
     *  Returns the integer value of month
     */
    public static String getMonth(String date) {

        String month = date.substring(MONTH_START_INDEX, MONTH_END_INDEX);

        return month;

    }

    /**
     *  Returns the integer value of day
     */
    public static int getDay(String date) {

        String day = date.substring(DAY_START_INDEX);

        return Integer.parseInt(day);

    }

    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.date.equals(((Date) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

```
###### \java\seedu\address\model\appointment\Duration.java
``` java
/**
 * Represents an Appointment's duration in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDuration(String)}
 */
public class Duration {

    public static final String MESSAGE_DURATION_CONSTRAINTS =
            "Appointment duration should be all integers in format DD or DDD (in minute), "
                    + "ranging from 15-120 minutes and it should not be blank";

    /*
     * The first character of the date must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DURATION_VALIDATION_REGEX = "(1[5-9]|[2-9][0-9]|1[01][0-9]|120)";

    public final String duration;

    /**
     * Constructs a {@code Duration}.
     *
     * @param duration A valid duration.
     */
    public Duration(String duration) {
        requireNonNull(duration);
        checkArgument(isValidDuration(duration), MESSAGE_DURATION_CONSTRAINTS);
        this.duration = duration;
    }

    public Duration(int duration) {
        String durationString = "" + duration;
        checkArgument(isValidDuration(durationString), MESSAGE_DURATION_CONSTRAINTS);
        this.duration = durationString;
    }

    /**
     * Returns the integer value of duration
     */
    public int getDurationValue() {
        return Integer.parseInt(this.duration);
    }

    /**
     * Returns true if a given string is a valid duration.
     */
    public static boolean isValidDuration(String test) {
        return test.matches(DURATION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return duration;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Duration // instanceof handles nulls
                && this.duration.equals(((Duration) other).duration)); // state check
    }

    @Override
    public int hashCode() {
        return duration.hashCode();
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\AppointmentCloseToNextException.java
``` java
/**
 *Signals that the operation will result the newly scheduled appointment falls
 * within the duration of the next appointment
 */
public class AppointmentCloseToNextException extends Exception {
    public AppointmentCloseToNextException(String message) {
        super(message);
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\AppointmentCloseToPreviousException.java
``` java
/**
 *Signals that the operation will result the newly scheduled appointment falls
 * within the duration of previous appointment
 */
public class AppointmentCloseToPreviousException extends Exception {
    public AppointmentCloseToPreviousException(String message) {
        super(message);
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\AppointmentHasBeenTakenException.java
``` java
/**
 * Signals that the appointment has been taken by a pet.
 */
public class AppointmentHasBeenTakenException extends Exception {
}
```
###### \java\seedu\address\model\appointment\exceptions\AppointmentListIsEmptyException.java
``` java
/**
 * Throws an exception if the appointment list is empty when a user tries to ammend it.
 */
public class AppointmentListIsEmptyException extends Exception {
}
```
###### \java\seedu\address\model\appointment\exceptions\AppointmentNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified appointment.
 */
public class AppointmentNotFoundException extends Exception {}
```
###### \java\seedu\address\model\appointment\exceptions\DuplicateAppointmentException.java
``` java
/**
 * Signals that the operation will result in duplicate(clashed) appointment objects.
 */
public class DuplicateAppointmentException extends DuplicateDataException {
    public DuplicateAppointmentException() {
        super("Operation would result in appointments clashes");
    }
}
```
###### \java\seedu\address\model\appointment\Time.java
``` java
/**
 * Represents an Appointment's time in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_TIME_CONSTRAINTS =
            "Appointment time should be all integers in format HH:MM, and it should not be blank";

    /*
     * The first character of the time must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String TIME_VALIDATION_REGEX = "([01]?[0-9]|2[0-3]):([0-5][0-9])";


    private static final int HOUR_START_INDEX = 0;
    private static final int HOUR_END_INDEX = 2;
    private static final int MINUTE_START_INDEX = 3;

    public final String time;

    /**
     * Constructs a {@code Time}.
     *
     * @param time A valid time.
     */
    public Time(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_TIME_CONSTRAINTS);
        this.time = time;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidTime(String test) {
        return test.matches(TIME_VALIDATION_REGEX);
    }

    /**
     *  Returns the integer value of the Minute in time
     */
    public int getMinute() {

        String minute = this.toString().substring(MINUTE_START_INDEX);

        return Integer.parseInt(minute);
    }

    /**
     *  Returns the integer value of Hour in time
     */
    public int getHour() {

        String hour = this.toString().substring(HOUR_START_INDEX, HOUR_END_INDEX);

        return Integer.parseInt(hour);

    }

    @Override
    public String toString() {
        return time;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Time // instanceof handles nulls
                && this.time.equals(((Time) other).time)); // state check
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }

```
###### \java\seedu\address\model\appointment\UniqueAppointmentList.java
``` java
/**
 * A list of appointments that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Appointment#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueAppointmentList implements Iterable<Appointment> {
    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an appointment with the same date and time as the given argument.
     */
    public boolean contains(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an appointment to the list.
     *
     * @throws DuplicateAppointmentException if the appointment to add is a duplicate(same date and time)
     * of an existing appointment in the list.
     */
    public void add(Appointment toAdd) throws DuplicateAppointmentException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAppointmentException();
        }
        internalList.add(toAdd);
    }

    /**
     * Reschedule the appointment {@code target} in the list with {@code editedAppointment}.
     *
     * @throws DuplicateAppointmentException if the reschedule clashes to another existing appointment in the list.
     * @throws AppointmentNotFoundException if {@code target} could not be found in the list.
     */
    public void setAppointment(Appointment target, Appointment editedAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireNonNull(editedAppointment);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new AppointmentNotFoundException();
        }
        if (!target.equals(editedAppointment) && internalList.contains(editedAppointment)) {
            throw new AppointmentNotFoundException();
        }
        internalList.set(index, editedAppointment);
    }

    /**
     * Removes the appointment from the list.
     *
     * @throws AppointmentNotFoundException if no such appointment could be found in the list.
     */
    public boolean remove(Appointment toRemove) throws AppointmentNotFoundException {
        requireNonNull(toRemove);
        final boolean appointmentFoundAndDeleted = internalList.remove(toRemove);
        if (!appointmentFoundAndDeleted) {
            throw new AppointmentNotFoundException();
        }
        return appointmentFoundAndDeleted;
    }

    public void setAppointments(UniqueAppointmentList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setAppointments(List<Appointment> appointments) throws DuplicateAppointmentException {
        requireAllNonNull(appointments);
        final UniqueAppointmentList replacement = new UniqueAppointmentList();
        for (final Appointment appointment : appointments) {
            replacement.add(appointment);
        }
        setAppointments(replacement);
    }

```
