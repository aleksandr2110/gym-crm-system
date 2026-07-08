package epam.domain;

public enum TrainingTypeName {

    JAVA("Java"), C("C"), PYTHON("Python"), ANGULAR("Angular"), REACT("React"),
    JAVASCRIPT("Javascript"), TYPESCRIPT("Typescript");

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
            default -> TrainingTypeName.TYPESCRIPT;
        };
        return trainingTypeName;
    }
}
