package garg.sarthik.construction_prototype_v2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectAdaptor extends RecyclerView.Adapter<ProjectAdaptor.ViewHolder> {

    Context ctx;
    ArrayList<Project> projectData;
    String loginType;

    public ProjectAdaptor(Context ctx, ArrayList<Project> projectData, String loginType) {
        this.ctx = ctx;
        this.projectData = projectData;
        this.loginType = loginType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_pro_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Project project = projectData.get(i);
        viewHolder.tvItemProName.setText(project.getProName());
        if (loginType.equals(Constants.COL_CONT))
            viewHolder.tvItemName.setText(project.getUser().getUserName());
        else
            viewHolder.tvItemName.setText(project.getContractor().getContName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctx.startActivity(new Intent(ctx,UserProjectActivity.class).putExtra(loginType,project));
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemProName;
        TextView tvItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemProName = itemView.findViewById(R.id.tvItemProName);
        }
    }
}
