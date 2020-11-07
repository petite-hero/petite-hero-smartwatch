package hero.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import hero.data.TaskDTO;
import hero.main.R;

public class TaskAdapter extends ArrayAdapter<TaskDTO> {

    private Activity activity;
    private List<TaskDTO> taskList;
    private static LayoutInflater inflater = null;

    public TaskAdapter(Activity activity, int textViewResourceId, List<TaskDTO> taskList) {
        super(activity, textViewResourceId, taskList);
        this.activity = activity;
        this.taskList = taskList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        public View taskContainer;
        public View taskFrontFaceContainer;
        public View taskBackFaceContainer;
        public ImageView imvIconTaskType;
        public TextView txtName;
        public TextView txtFrom;
        public TextView txtTo;
        public TextView txtDetail;
        public View iconCameraContainer;
        public ImageView imvIconCamera;
        public ImageView imvProof;
        public TextView txtStatus;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // get needed components
        View vi = inflater.inflate(R.layout.item_task, null);
        final ViewHolder holder = new ViewHolder();
        holder.taskContainer = vi.findViewById(R.id.taskContainer);
        holder.taskFrontFaceContainer = vi.findViewById(R.id.taskFrontFaceContainer);
        holder.taskBackFaceContainer = vi.findViewById(R.id.taskBackFaceContainer);
        holder.imvIconTaskType = vi.findViewById(R.id.imvIconTaskType);
        holder.txtName = vi.findViewById(R.id.txtName);
        holder.txtFrom = vi.findViewById(R.id.txtFrom);
        holder.txtTo = vi.findViewById(R.id.txtTo);
        holder.txtDetail = vi.findViewById(R.id.txtDetail);
        holder.iconCameraContainer = vi.findViewById(R.id.iconCameraContainer);
        holder.imvIconCamera = vi.findViewById(R.id.imvIconCamera);
        holder.imvProof = vi.findViewById(R.id.imvProof);
        holder.txtStatus = vi.findViewById(R.id.txtStatus);
        TaskDTO task = taskList.get(position);

        // get associated background color, icon & radius
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

        // set layout attributes
        PaintDrawable pd = new PaintDrawable(activity.getResources().getColor(colorId));
        pd.setCornerRadius(20);
        holder.taskFrontFaceContainer.setBackground(pd);
        holder.imvIconTaskType.setImageResource(iconId);

        pd = new PaintDrawable(Color.WHITE);
        pd.setCornerRadius(20);
        holder.taskBackFaceContainer.setBackground(pd);

        pd = new PaintDrawable(activity.getResources().getColor(colorId));
        pd.setCornerRadius(35);
        holder.imvIconCamera.setBackground(pd);

        // set data attributes
        holder.txtName.setText(task.getName());
        holder.txtFrom.setText("From " + task.getFromTime().get(Calendar.HOUR_OF_DAY) + ":" + task.getFromTime().get(Calendar.MINUTE));
        holder.txtTo.setText("To " + task.getToTime().get(Calendar.HOUR_OF_DAY) + ":" + task.getToTime().get(Calendar.MINUTE));
        holder.txtDetail.setText(task.getDetail());

        // back
        if (!task.getStatus().equals("ASSIGNED")){

            holder.txtStatus.setText(task.getStatus());

            holder.txtDetail.setVisibility(View.GONE);
            holder.iconCameraContainer.setVisibility(View.GONE);
            holder.imvProof.setVisibility(View.VISIBLE);

            byte[] decodedString = Base64.decode(task.getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imvProof.setImageBitmap(decodedByte);

        }

        // apply to view
        vi.setTag(holder);
        return vi;

    }
}