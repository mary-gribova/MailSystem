package mailSystem.client.mailBox.folders;

import mailSystem.client.mailBox.lettersList.Letter;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Folder {
    private String name;
    private List<Letter> letters;
    private String model;

    private String getRandomModel() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

    public String getName() {
        return name;
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public void parseFolderResult(JSONObject folder) {
        try {
            letters = new ArrayList<Letter>();
            name = folder.getString("folderName");
            model = getRandomModel();
            JSONArray jsonLetters = folder.getJSONArray("letters");

            for (int i = 0; i < jsonLetters.length(); i++) {
                Letter letter = new Letter();
                letter.parseLetterResult(jsonLetters.getJSONObject(i));
                letters.add(letter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Folder(String name) {
        this.letters = new ArrayList<Letter>();
        this.name = name;
        this.model = getRandomModel();
    }

    public Folder() {
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Folder && name.equals(((Folder) obj).getName());
    }

    public Folder(Folder folder) {
        this.name = folder.getName();
        this.letters = folder.getLetters();
        this.model = folder.getModel();
    }
}
