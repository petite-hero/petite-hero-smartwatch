package hero.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import hero.data.TaskDTO;
import hero.main.MainScreenActivity;
import hero.main.R;
import hero.util.Util;

public class TaskAdapter extends ArrayAdapter<TaskDTO> {

    private Activity activity;
    private List<TaskDTO> taskList;
    private static LayoutInflater inflater = null;
    private boolean isActive;

    public TaskAdapter(Activity activity, int textViewResourceId, List<TaskDTO> taskList, boolean isActive) {
        super(activity, textViewResourceId, taskList);
        this.activity = activity;
        this.taskList = taskList;
        this.isActive = isActive;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // DECLARE NEEDED COMPONENTS
        View vi = inflater.inflate(R.layout.item_task, null);
        Resources resource = activity.getResources();
        final TaskDTO task = taskList.get(position);

        View taskFrontFaceContainer = vi.findViewById(R.id.taskFrontFaceContainer);
        View taskBackFaceContainer = vi.findViewById(R.id.taskBackFaceContainer);
        ImageView imvIconTaskType = vi.findViewById(R.id.imvIconTaskType);
        TextView txtName = vi.findViewById(R.id.txtName);
        TextView txtTo = vi.findViewById(R.id.txtTo);
        View decorLeft = vi.findViewById(R.id.decorLeft);
        TextView txtDetail = vi.findViewById(R.id.txtDetail);
        ImageView imvIconCamera = vi.findViewById(R.id.imvIconCamera);
        View decorLeftBack = vi.findViewById(R.id.decorLeftBack);

        // SET LAYOUT ATTRIBUTES

        // get background color, icon & radius on task type
        int colorId = 0;
        int iconId = 0;
        if (task.getType().equals("Housework")){
            colorId = R.color.colorTaskCategoryHousehold;
            iconId = R.drawable.icon_home;
        }
        else if (task.getType().equals("Education")){
            colorId = R.color.colorTaskCategoryHomework;
            iconId = R.drawable.icon_school;
        }
        else if (task.getType().equals("Skills")){
            colorId = R.color.colorTaskCategorySkill;
            iconId = R.drawable.icon_fan;
        }

        // set layout for FRONT FACE CONTAINER
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(resource.getColor(R.color.colorTaskBackground));
        if (this.isActive) gd.setStroke(2, resource.getColor(colorId));
        else gd.setStroke(2, resource.getColor(R.color.colorTaskLate));
        gd.setCornerRadius(20);
        taskFrontFaceContainer.setBackground(gd);

        // set layout for "task type" icon
        imvIconTaskType.setImageResource(iconId);
        if (this.isActive) imvIconTaskType.setColorFilter(resource.getColor(colorId));
        else imvIconTaskType.setColorFilter(resource.getColor(R.color.colorTaskLate));

        // set layout for texts
        if (!this.isActive){
            txtName.setTextColor(resource.getColor(R.color.colorTaskLate));
            txtTo.setTextColor(resource.getColor(R.color.colorTaskLate));
            txtDetail.setTextColor(resource.getColor(R.color.colorTaskLate));
        }
        txtName.setTypeface(ResourcesCompat.getFont(activity, R.font.acumin_b));
        txtTo.setTypeface(ResourcesCompat.getFont(activity, R.font.acumin));
        txtDetail.setTypeface(ResourcesCompat.getFont(activity, R.font.acumin));

        // set layout for left decoration
        PaintDrawable pd;
        if (this.isActive) pd = new PaintDrawable(resource.getColor(colorId));
        else pd = new PaintDrawable(resource.getColor(R.color.colorTaskLate));
        pd.setCornerRadii(new float[] {10,20,0,0,0,0,10,20});
        decorLeft.setBackground(pd);

        // set layout for BACK FACE CONTAINER
        gd = new GradientDrawable();
        gd.setColor(resource.getColor(R.color.colorTaskBackground));
        if (this.isActive) gd.setStroke(2, resource.getColor(colorId));
        else gd.setStroke(2, resource.getColor(R.color.colorTaskLate));
        gd.setCornerRadius(20);
        taskBackFaceContainer.setBackground(gd);

        // set layout for "camera" icon
        pd = new PaintDrawable(resource.getColor(colorId));
        pd.setCornerRadius(35);
        imvIconCamera.setBackground(pd);

        // set layout for left decoration
        if (this.isActive) pd = new PaintDrawable(resource.getColor(colorId));
        else pd = new PaintDrawable(resource.getColor(R.color.colorTaskLate));
        pd.setCornerRadii(new float[] {10,20,0,0,0,0,10,20});
        decorLeftBack.setBackground(pd);

        // SET DATA ATTRIBUTES
        txtName.setText(task.getName());
        txtTo.setText("Đến " + Util.calendarToTimeStr(task.getToTime()));
        txtDetail.setText(task.getDetail());

        // SET TAKE IMAGE ICON CLICK LISTENER
        imvIconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainScreenActivity) activity).takeProofImage(task.getId());
            }
        });

        // apply to view
        return vi;

    }
}