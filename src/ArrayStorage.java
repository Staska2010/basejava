import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int numberOfResumes = 0;
    int positionOfResumeInStorage = 0;

    void clear() {
        for (int counter = 0; counter <= numberOfResumes; counter++) {
            storage[counter] = null;
        }
        numberOfResumes = 0;
    }

    void save(Resume r) {
        if (numberOfResumes < 9999) {
            storage[numberOfResumes] = r;
            numberOfResumes++;
        } else {
            System.out.println("Not enough space in storage");
        }
    }

    Resume get(String uuid) {
        Resume findResume = null;
        for (int counter = 0; counter < numberOfResumes; counter++) {
            if (storage[counter].uuid.equals(uuid)) {
                findResume = storage[counter];
                positionOfResumeInStorage = counter;
                break;
            }
        }
        if (findResume == null) {
            System.out.println("No such resume in storage: ");
        }
        return findResume;
    }

    void delete(String uuid) {
        Resume deleteResume = get(uuid);
        if (deleteResume != null) {
            for (int counter = positionOfResumeInStorage; counter < numberOfResumes; counter++) {
                storage[counter] = storage[counter + 1];
            }
            numberOfResumes--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, numberOfResumes);
    }

    int size() {
        return numberOfResumes;
    }
}
