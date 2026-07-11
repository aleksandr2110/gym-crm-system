package epam.domain;

public enum TrainingTypeName {

    JAVA("Java"), C("C"), PYTHON("Python"), ANGULAR("Angular"), REACT("React"),
    JAVASCRIPT("Javascript"), TYPESCRIPT("Typescript"), PHP("Php"), BA("Business Analyst");

    private final String name;

    TrainingTypeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TrainingTypeName getByName(String name) {
        TrainingTypeName trainingTypeName = switch (name) {
            case "JAVA" -> TrainingTypeName.JAVA;
            case "C" -> TrainingTypeName.C;
            case "PYTHON" -> TrainingTypeName.PYTHON;
            case "ANGULAR" -> TrainingTypeName.ANGULAR;
            case "REACT" -> TrainingTypeName.REACT;
            case "JAVASCRIPT" -> TrainingTypeName.JAVASCRIPT;
            case "TYPESCRIPT" -> TrainingTypeName.TYPESCRIPT;
            case "PHP" -> TrainingTypeName.PHP;
            default -> TrainingTypeName.BA;
        };
        return trainingTypeName;
    }
}
