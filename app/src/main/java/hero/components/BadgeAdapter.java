package hero.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import hero.data.QuestDTO;
import hero.main.R;
import hero.util.Util;

public class BadgeAdapter extends ArrayAdapter<QuestDTO> {

    private Activity activity;
    private List<QuestDTO> badgeList;
    private static LayoutInflater inflater = null;

    public BadgeAdapter(Activity activity, int textViewResourceId, List<QuestDTO> badgeList) {
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
        TextView txtTitle = vi.findViewById(R.id.txtTitle);

        // SET LAYOUT ATTRIBUTES

        // badge image
        imvIconBadge.setImageResource(resource.getIdentifier(Util.badgeIdToName(badgeList.get(position).getBadge()), "drawable", activity.getPackageName()));
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#f7f7f7"));
        // gd.setStroke(2, resource.getColor(R.color.colorLightCyan));
        gd.setCornerRadius(25);
        imvIconBadge.setBackground(gd);

        // badge title
//        String[] tmpTitleList = new String[] {"Người nhện", "Dũng sĩ", "Thần đồng", "Thợ lặn", "Siêng năng", "Chăm làm"};
//        txtTitle.setText(tmpTitleList[position]);
        txtTitle.setText(badgeList.get(position).getTitle());
        txtTitle.setTypeface(ResourcesCompat.getFont(activity, R.font.acumin));

        // apply to view
        return vi;

    }
}