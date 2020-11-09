package hero.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.PaintDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import hero.main.R;

public class BadgeAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private List<String> badgeList;
    private static LayoutInflater inflater = null;

    public BadgeAdapter(Activity activity, int textViewResourceId, List<String> badgeList) {
        super(activity, textViewResourceId, badgeList);
        this.activity = activity;
        this.badgeList = badgeList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // DECLARE NEEDED COMPONENTS
        View vi = inflater.inflate(R.layout.item_badge, null);
        Resources resource = activity.getResources();
        String badgeName = badgeList.get(position);
        ImageView imvIconBadge = vi.findViewById(R.id.badge);

        // SET LAYOUT ATTRIBUTES
        imvIconBadge.setImageResource(resource.getIdentifier(badgeName, "drawable", activity.getPackageName()));
        PaintDrawable pd = new PaintDrawable(resource.getColor(R.color.colorTaskBackground));
        pd.setCornerRadius(20);
//        imvIconBadge.setBackground(pd);

        // apply to view
        return vi;

    }
}