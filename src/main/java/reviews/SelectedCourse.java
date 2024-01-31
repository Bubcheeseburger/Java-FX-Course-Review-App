package reviews;

public class SelectedCourse {
    private static Course selectedCourse;

    public static Course getSelectedCourse() {
        return selectedCourse;
    }

    public static void setSelectedCourse(Course course) {
        selectedCourse = course;
    }
}
