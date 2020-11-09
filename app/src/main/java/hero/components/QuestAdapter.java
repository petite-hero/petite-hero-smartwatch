package hero.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hero.data.QuestDTO;
import hero.main.R;

public class QuestAdapter extends ArrayAdapter<QuestDTO> {

    private Activity activity;
    private List<QuestDTO> questList;
    private static LayoutInflater inflater = null;

    public QuestAdapter(Activity activity, int textViewResourceId, List<QuestDTO> questList) {
        super(activity, textViewResourceId, questList);
        this.activity = activity;
        this.questList = questList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // DECLARE NEEDED COMPONENTS
        View vi = inflater.inflate(R.layout.item_quest, null);
        Resources resource = activity.getResources();
        QuestDTO quest = questList.get(position);

        View questFrontFaceContainer = vi.findViewById(R.id.questFrontFaceContainer);
        View questBackFaceContainer = vi.findViewById(R.id.questBackFaceContainer);
        ImageView imvIconBadge = vi.findViewById(R.id.imvIconBadge);
        TextView txtName = vi.findViewById(R.id.txtName);
        View decorLeft = vi.findViewById(R.id.decorLeft);
        TextView txtDetail = vi.findViewById(R.id.txtDetail);
        View decorLeftBack = vi.findViewById(R.id.decorLeftBack);


        // SET LAYOUT ATTRIBUTES

        // set layout for FRONT FACE CONTAINER
        PaintDrawable pd;
        pd = new PaintDrawable(resource.getColor(R.color.colorTaskBackground));
        pd.setCornerRadius(20);
        questFrontFaceContainer.setBackground(pd);

        // set icon for badge
        imvIconBadge.setImageResource(resource.getIdentifier(quest.getBadgeName(), "drawable", activity.getPackageName()));

        // set layout for left decoration
        pd = new PaintDrawable(Color.parseColor(quest.getColor()));
        pd.setCornerRadii(new float[] {10,20,0,0,0,0,10,20});
        decorLeft.setBackground(pd);

        // set layout for BACK FACE CONTAINER
        pd = new PaintDrawable(resource.getColor(R.color.colorTaskBackground));
        pd.setCornerRadius(20);
        questBackFaceContainer.setBackground(pd);

        // set layout for left decoration
        pd = new PaintDrawable(Color.parseColor(quest.getColor()));
        pd.setCornerRadii(new float[] {10,20,0,0,0,0,10,20});
        decorLeftBack.setBackground(pd);

        // SET DATA ATTRIBUTES
        txtName.setText(quest.getName());
        txtDetail.setText(quest.getDetail());

        // apply to view
        return vi;

    }
}