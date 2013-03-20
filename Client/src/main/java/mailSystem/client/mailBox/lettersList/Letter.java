package mailSystem.client.mailBox.lettersList;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Letter {
    private String from;
    private String theme;
    private String date;
    private String body;
    private boolean checked;
    private String model;
    private String folder;
    private String to;
    private boolean newLetter;
    private Date realDate;

    private static final Logger LOG = Logger.getLogger(Letter.class.getName());

    public Date getRealDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setRealDate(Date realDate) {
        try {
            this.realDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setNewLetter(boolean newLetter) {
        this.newLetter = newLetter;
    }

    public boolean isNewLetter() {
        return newLetter;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void parseLetterResult(JSONObject letter) {
        try {
            from = letter.getString("from");
            theme = letter.getString("theme");
            body = letter.getString("body");
            date = letter.getString("date");
            folder = letter.getString("folder");
            to = letter.getString("to");
            newLetter = letter.getBoolean("new");

            checked = false;
            model = getRandomModel();

        } catch (JSONException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "Error while getting fields from json object");
        }
    }

    private String getRandomModel() {
        return UUID.randomUUID().toString().substring(0, 8);
    }


    @Override
    public boolean equals(Object obj) {
       if (obj instanceof Letter) {
           Letter letter = (Letter) obj;
           return (this.folder.equals(letter.getFolder()) && this.body.equals(letter.getBody()) &&
                   this.date.equals(letter.getDate()) && this.from.equals(letter.from) && this.theme.equals(letter.theme));
       }  else {
           return false;
       }
    }

}
