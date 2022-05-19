package redcoder.texteditor.core.findreplace;

enum Type {
    FIND("Find"),
    REPLACE("Replace");

    final String title;

    Type(String title) {
        this.title = title;
    }
}
