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
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
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
    public static final String MESSAGE_CANNOT_SCHEDULE_AT_THIS_TIME = "The new appointment is within"
            + " the duration of another appointment, you need at least 15 minutes for an appointment\n";
    public static final String MESSAGE_SUGGESTION_DURATION = "This appointment can last at most: ";
    private static final String MINUTE_SUFFIX = " minutes";
    private static final int MINIMUM_INTERVAL = 1440;
    private static final int CORRECT_DURATION = 120;

    private final Appointment toAdd;

    /**
     * Creates an ScheduleCommand to add the specified {@code appointment}
     */
    public ScheduleCommand(Appointment appointment) {
        requireNonNull(appointment);

        this.toAdd = appointment;
    }

    /**
     * Returns an integer value of duration
     */
    public int getSuggestedDelayDuration(ObservableList<Appointment> existingAppointmentList,
                                              Appointment appointment) {
        Date newAppointmentDate = appointment.getDate();
        Time newAppointmentTime = appointment.getTime();
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
                    interval = appointment.calDurationDifferencePositive(earlierAppointment);
                    if (interval < minInterval) {
                        minInterval = interval;
                        correctDuration = calInterval(earlierAppointment.getDuration().getDurationValue(), minInterval);
                    }
                }
            }
        }
        return correctDuration;
    }

    /**
     * Returns an integer value of duration.
     */
    public int getSuggestedMaxDuration(ObservableList<Appointment> existingAppointmentList,
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

                    interval = appointment.calDurationDifferenceNegative(laterAppointment);
                    if (interval < minInterval) {
                        minInterval = interval;
                    }
                }
            }
        }
        return minInterval;
    }

    /**
     * Return the interval between two given integer values
     */
    public int calInterval(int first, int second) {
        return first - second;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.scheduleAppointment(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateAppointmentException e1) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentCloseToPreviousException e2) {
            int suggestedDelayDuration = getSuggestedDelayDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_PREVIOUS + MESSAGE_SUGGESTION_TIME
                    + Integer.toString(suggestedDelayDuration) + MINUTE_SUFFIX);
        } catch (AppointmentCloseToNextException e3) {
            int suggestedMaxDuration = getSuggestedMaxDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_NEXT + MESSAGE_SUGGESTION_DURATION
                        + Integer.toString(suggestedMaxDuration) + MINUTE_SUFFIX);
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
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String date} into a {@code Date}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code date} is invalid.
     */
    public static Date parseDate(String date) throws IllegalValueException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!Date.isValidDate(trimmedDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        if (!Date.isValidYear(Date.getYear(trimmedDate))) {
            throw new IllegalValueException(Date.MESSAGE_YEAR_CONSTRAINTS);
        }
        if (!Date.isValidDaysInMonth(trimmedDate)) {
            throw new IllegalValueException(Date.MESSAGE_DAYINMONTH_CONSTRAINTS);
        }
        return new Date(trimmedDate);
    }

    /**
     * Parses a {@code Optional<String> date} into an {@code Optional<Date>} if {@code date} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Date> parseDate(Optional<String> date) throws IllegalValueException {
        requireNonNull(date);
        return date.isPresent() ? Optional.of(parseDate(date.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String time} into a {@code Time}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code time} is invalid.
     */
    public static Time parseTime(String time) throws IllegalValueException {
        requireNonNull(time);
        String trimmedTime = time.trim();
        if (!Time.isValidTime(trimmedTime)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        return new Time(trimmedTime);
    }

    /**
     * Parses a {@code Optional<String> time} into an {@code Optional<Time>} if {@code time} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Time> parseTime(Optional<String> time) throws IllegalValueException {
        requireNonNull(time);
        return time.isPresent() ? Optional.of(parseTime(time.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String duration} into a {@code Duration}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code duration} is invalid.
     */
    public static Duration parseDuration(String duration) throws IllegalValueException {
        requireNonNull(duration);
        String trimmedDuration = duration.trim();
        if (!Duration.isValidDuration(trimmedDuration)) {
            throw new IllegalValueException(Duration.MESSAGE_DURATION_CONSTRAINTS);
        }
        return new Duration(trimmedDuration);
    }

    /**
     * Parses a {@code Optional<String> duration} into an {@code Optional<Duration>} if {@code duration} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Duration> parseDuration(Optional<String> duration) throws IllegalValueException {
        requireNonNull(duration);
        return duration.isPresent() ? Optional.of(parseDuration(duration.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String description} into a {@code Description}
     * leading and trailing whitespaces will be trimmed.
     */
    public static Description parseDescription(String description) throws IllegalValueException {
        requireNonNull(description);
        return new Description(description.trim());
    }

    /**
     * Parses a {@code Optional<String> description} into an {@code Optional<Description>}
     * if {@code description} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Description> parseDescription(Optional<String> description) throws IllegalValueException {
        requireNonNull(description);
        return description.isPresent() ? Optional.of(parseDescription(description.get())) : Optional.empty();
    }
```
###### \java\seedu\address\logic\parser\RescheduleCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class RescheduleCommandParser implements Parser<RescheduleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RescheduleCommand
     * and returns an RescheduleCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RescheduleCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME, PREFIX_DURATION, PREFIX_DESCRIPTION);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RescheduleCommand.MESSAGE_USAGE));
        }

        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptor();
        try {
            ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE)).ifPresent(descriptor::setDate);
            ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME)).ifPresent(descriptor::setTime);
            ParserUtil.parseDuration(argMultimap.getValue(PREFIX_DURATION)).ifPresent(descriptor::setDuration);
            ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).ifPresent(descriptor::setDescription);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(RescheduleCommand.MESSAGE_NOT_RESCHEDULED);
        }

        return new RescheduleCommand(index, descriptor);
    }

}
```
###### \java\seedu\address\logic\parser\ScheduleCommandParser.java
``` java
/**
 * Parses input arguments and creates a new {@code ScheduleCommand} object
 */
public class ScheduleCommandParser implements Parser<ScheduleCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code ScheduleCommand}
     * and returns a {@code ScheduleCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ScheduleCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME,
                PREFIX_DURATION, PREFIX_DESCRIPTION);

        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_TIME, PREFIX_DURATION, PREFIX_DESCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE));
        }
        try {
            Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE)).get();
            Time time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME)).get();
            Duration duration = ParserUtil.parseDuration(argMultimap.getValue(PREFIX_DURATION).get());
            Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
            Appointment appointment = new Appointment(date, time, duration, description);

            return new ScheduleCommand(appointment);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Schedule an appointment to the address book.
     *
     * @throws DuplicateAppointmentException if an equivalent person already exists.
     */
    public void scheduleAppointment(Appointment a) throws DuplicateAppointmentException,
            AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        appointments.add(a);
        appointments.sort();
    }

    /**
     * Replaces the given appointment {@code target} in the list with {@code rescheduleAppointment}.
     *
     * @throws DuplicateAppointmentException if updating the appointment's details causes this appointment to clash with
     *      another existing appointment in the list.
     * @throws AppointmentNotFoundException if {@code target} could not be found in the list.
     *
     */
    public void updateAppointment(Appointment target, Appointment rescheduleAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireNonNull(rescheduleAppointment);

        appointments.setAppointment(target, rescheduleAppointment);
    }
```
###### \java\seedu\address\model\appointment\Appointment.java
``` java
/**
 * Represents an Appointment in the application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Appointment {

    private static final int CONVERSION_TIME = 60;
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

    /**
     * Returns the interval in minutes between two appointments
     */
    public int calDurationDifferencePositive(Appointment previous) {
        Time previousTime = previous.getTime();

        return hourDifference(this.time.getHour(), previousTime.getHour())
                + minDifference(this.time.getMinute(), previousTime.getMinute());
    }

    /**
     * Returns the interval in minutes between two appointments
     */
    public int calDurationDifferenceNegative(Appointment next) {
        Time nextTime = next.getTime();

        return hourDifference(nextTime.getHour(), this.time.getHour())
                + minDifference(nextTime.getMinute(), this.time.getMinute());
    }

    /**
     * Returns the difference in Hour of time between two appointments
     */
    public int hourDifference(int hourFirst, int hourSecond) {
        return Math.abs((hourFirst - hourSecond) * CONVERSION_TIME);
    }

    /**
     * Returns the difference in Minute of time between two appointments
     */
    public int minDifference(int minFirst, int minSecond) {
        return minFirst - minSecond;
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

    public Duration(int duration) throws AppointmentCloseToNextException {
        String durationString = "" + duration;
        if (!isValidDuration(durationString)) {
            throw new AppointmentCloseToNextException("Appointment cannot be scheduled at this duration");
        }
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
    public static final String MESSAGE_DURATION_PREVIOUS = " Appointment is too close to previous one.";
    public static final String MESSAGE_DURATION_NEXT = " Appointment is too close to next one.";
    private static final int MINIMUM_INTERVAL = 1440;
    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();
    private Appointment previous;
    private Appointment next;

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
    public void add(Appointment toAdd) throws DuplicateAppointmentException,
        AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAppointmentException();
        }
        if (hasDurationClosePrevious(toAdd)) {
            throw new AppointmentCloseToPreviousException(MESSAGE_DURATION_PREVIOUS);
        }
        if (hasDurationCloseNext(toAdd)) {
            throw new AppointmentCloseToNextException(MESSAGE_DURATION_NEXT);
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
            throw new DuplicateAppointmentException();
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

    public void setAppointments(List<Appointment> appointments) throws DuplicateAppointmentException,
            AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        requireAllNonNull(appointments);
        final UniqueAppointmentList replacement = new UniqueAppointmentList();
        for (final Appointment appointment : appointments) {
            replacement.add(appointment);
        }
        setAppointments(replacement);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException,
            AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        addressBook.scheduleAppointment(appointment);
        updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void updateAppointment(Appointment target, Appointment rescheduleAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireAllNonNull(target, rescheduleAppointment);
        addressBook.updateAppointment(target, rescheduleAppointment);
        indicateAddressBookChanged();
        if (displayAppt != null && displayAppt.contains(target)) {
            displayAppt.set(displayAppt.indexOf(target), rescheduleAppointment);
            indicateListAllPanelChanged();
        }
    }

```
###### \java\seedu\address\storage\XmlAdaptedAppointment.java
``` java
/**
 * JAXB-friendly version of the Appointment.
 */
public class XmlAdaptedAppointment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Appointment's %s field is missing!";

    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String time;
    @XmlElement(required = true)
    private String duration;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private XmlAdaptedClientOwnPet association;
    @XmlElement(required = true)
    private XmlAdaptedPerson vetTech;

    /**
     * Constructs an XmlAdaptedAppointment.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAppointment() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedAppointment(String date, String time, String duration, String description) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.description = description;
    }

    /**
     * Converts a given Appointment into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedAppointment
     */
    public XmlAdaptedAppointment(Appointment source) {
        date = source.getDate().toString();
        time = source.getTime().toString();
        duration = source.getDuration().toString();
        description = source.getDescription().toString();
        if (source.getClientOwnPet() != null) {
            association = new XmlAdaptedClientOwnPet(source.getClientOwnPet());
        }
        if (source.getVetTechnician() != null) {
            vetTech = new XmlAdaptedPerson(source.getVetTechnician());
        }
    }

    /**
     * Converts this jaxb-friendly adapted appointment object into the model's Appointment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted appointment
     */
    public Appointment toModelType() throws IllegalValueException {

        Appointment convertedAppointment;

        if (this.date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.date)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        final Date date = new Date(this.date);

        if (this.time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(this.time)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        final Time time = new Time(this.time);

        if (this.duration == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Duration.class.getSimpleName()));
        }
        if (!Duration.isValidDuration(this.duration)) {
            throw new IllegalValueException(Duration.MESSAGE_DURATION_CONSTRAINTS);
        }
        final Duration duration = new Duration(this.duration);

        if (this.description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(this.description)) {
            throw new IllegalValueException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        final Description description = new Description(this.description);

        convertedAppointment = new Appointment(date, time, duration, description);

        if (this.association != null) {
            ClientOwnPet cop = association.toModelType();
            convertedAppointment.setClientOwnPet(cop);
        }

        if (this.vetTech != null) {
            VetTechnician vetTech = (VetTechnician) this.vetTech.toModelType();
            convertedAppointment.setVetTech(vetTech);
        }

        return convertedAppointment;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAppointment)) {
            return false;
        }

        XmlAdaptedAppointment otherAppointment = (XmlAdaptedAppointment) other;
        return Objects.equals(date, otherAppointment.date)
                && Objects.equals(time, otherAppointment.time);

    }
}
```
