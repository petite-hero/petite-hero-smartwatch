package hero.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import hero.main.R;
import hero.util.Util;

public class BadgeAdapter extends ArrayAdapter<Integer> {

    private Activity activity;
    private List<Integer> badgeList;
    private static LayoutInflater inflater = null;

    public BadgeAdapter(Activity activity, int textViewResourceId, List<Integer> badgeList) {
        super(activity, textViewResourceId, badgeList);
        this.activity = activity;
        this.badgeList = badgeList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // DECLARE NEEDED COMPONENTS
        View vi = inflater.inflate(R.layout.item_badge, null);
        Resources resource = activity.getResources();
        ImageView imvIconBadge = vi.findViewById(R.id.badge);

        // SET LAYOUT ATTRIBUTES
        imvIconBadge.setImageResource(resource.getIdentifier(Util.badgeIdToName(badgeList.get(position)), "drawable", activity.getPackageName()));
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(resource.getColor(R.color.colorWhite));
        gd.setStroke(2, resource.getColor(R.color.colorLightCyan));
        gd.setCornerRadius(30);
        imvIconBadge.setBackground(gd);

        // apply to view
        return vi;

    }
}