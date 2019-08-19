import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int numberOfResumes = 0;
    int positionOfResumeInStorage = 0;

    void clear() {
        for (int i = 0; i < numberOfResumes; i++) {
            storage[i] = null;
        }
        numberOfResumes = 0;
    }

    void save(Resume r) {
        if (numberOfResumes < storage.length) {
            storage[numberOfResumes] = r;
            numberOfResumes++;
        } else {
            System.out.println("Not enough space in storage");
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < numberOfResumes; i++) {
            if (storage[i].uuid.equals(uuid)) {
                positionOfResumeInStorage = i;
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        Resume deleteResume = get(uuid);
        if (deleteResume != null) {
            for (int i = positionOfResumeInStorage; i < numberOfResumes - 1; i++) {
                storage[i] = storage[i + 1];
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
