package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Time;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.PersonRole;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.PetAge;
import seedu.address.model.pet.PetGender;
import seedu.address.model.pet.PetName;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INSUFFICIENT_PARTS = "Number of parts must be more than 1.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(parseName(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws IllegalValueException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(parsePhone(phone.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws IllegalValueException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Address> parseAddress(Optional<String> address) throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(parseAddress(address.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws IllegalValueException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**

     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(parseEmail(email.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws IllegalValueException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String role} into an {@code PersonRole} enum type.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code role} is invalid.
     */
    public static PersonRole parseRole(String role) throws IllegalValueException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!PersonRole.isValidPersonRole(trimmedRole)) {
            throw new IllegalValueException(PersonRole.MESSAGE_ROLE_CONSTRAINTS);
        }
        return new PersonRole(trimmedRole);
    }

    /**
     * Parses a {@code Optional<String> role} into an {@code Optional<PersonRole>} if {@code role} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<PersonRole> parseRole(Optional<String> role) throws IllegalValueException {
        requireNonNull(role);
        return role.isPresent()
               ? Optional.of(parseRole(role.get()))
               : Optional.empty();
    }

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
     * Parses a {@code String petName} into {@code PetName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code petName} is invalid.
     */
    public static PetName parsePetName(String petName) throws IllegalValueException {
        requireNonNull(petName);
        String trimmedPetName = petName.trim();
        if (!PetName.isValidPetName(trimmedPetName)) {
            throw new IllegalValueException(PetName.MESSAGE_PETNAME_CONSTRAINTS);
        }
        return new PetName(trimmedPetName);
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<PetName>}
     */
    public static Optional<PetName> parsePetName(Optional<String> petName) throws IllegalValueException {
        requireNonNull(petName);
        return petName.isPresent() ? Optional.of(parsePetName(petName.get())) : Optional.empty();
    }

    /**
     * Parses {@code String petAge} into {@code PetAge}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static PetAge parsePetAge(String petAge) throws IllegalValueException {
        requireNonNull(petAge);
        String trimmedPetAge = petAge.trim();
        if (!PetAge.isValidPetAge(trimmedPetAge)) {
            throw new IllegalValueException(PetAge.MESSAGE_PETAGE_CONSTRAINTS);
        }
        return new PetAge(trimmedPetAge);
    }

    /**
     * Parses a {@code Optional<String> petAge} into an {@code Optional<PetAge>}
     */
    public static Optional<PetAge> parsePetAge(Optional<String> petAge) throws IllegalValueException {
        requireNonNull(petAge);
        return petAge.isPresent() ? Optional.of(parsePetAge(petAge.get())) : Optional.empty();
    }

    /**
     * Parses {@code String petGender} into {@code PetGender}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static PetGender parsePetGender(String petGender) throws IllegalValueException {
        requireNonNull(petGender);
        String trimmedPetGender = petGender.trim();
        if (!PetGender.isValidGender(trimmedPetGender)) {
            throw new IllegalValueException(PetGender.MESSAGE_PETGENDER_CONSTRAINTS);
        }
        return new PetGender(trimmedPetGender);
    }

    /**
     * Parses a {@code Optional<String> petGender} into {@code PetGender}.
     */
    public static Optional<PetGender> parsePetGender(Optional<String> petGender) throws IllegalValueException {
        requireNonNull(petGender);
        return petGender.isPresent() ? Optional.of(parsePetGender(petGender.get())) : Optional.empty();
    }
}
