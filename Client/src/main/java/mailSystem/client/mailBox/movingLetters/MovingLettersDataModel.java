package mailSystem.client.mailBox.movingLetters;

import mailSystem.client.mailBox.folders.Folder;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class MovingLettersDataModel extends ListDataModel<Folder> implements SelectableDataModel<Folder> {
    public MovingLettersDataModel() {
    }

    public MovingLettersDataModel(List<Folder> data) {
        super(data);
    }

    @Override
    public Folder getRowData(String rowKey) {
        List<Folder> folders = (List<Folder>) getWrappedData();

        for(Folder folder : folders) {
            if(folder.getModel().equals(rowKey))
                return folder;
        }

        return null;
    }

    @Override
    public Object getRowKey(Folder folder) {
        return folder.getModel();
    }
}