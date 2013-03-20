package mailSystem.client.mailBox.lettersList;

import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class LetterDataModel extends ListDataModel<Letter> implements SelectableDataModel<Letter> {
    public LetterDataModel() {
    }

    public LetterDataModel(List<Letter> data) {
        super(data);
    }

    @Override
    public Letter getRowData(String rowKey) {
        List<Letter> letters = (List<Letter>) getWrappedData();

        for(Letter letter : letters) {
            if(letter.getModel().equals(rowKey))
                return letter;
        }

        return null;
    }

    @Override
    public Object getRowKey(Letter letter) {
        return letter.getModel();
    }
}
