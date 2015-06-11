package com.example.amado.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Amado on 11/06/2015.
 */
public class NerdLauncherFragment extends ListFragment {
    private static final String TAG ="NerdLauncherFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        Log.i(TAG, "found"+activities.size()+"activities");


        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                return String.CASE_INSENSITIVE_ORDER.compare(lhs.loadLabel(pm).toString(),
                        rhs.loadLabel(pm).toString());
            }
        });
        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(getActivity(),
                R.layout.list_item, R.id.appn_name,activities){
           public View getView(int pos, View convertView, ViewGroup parent){
               PackageManager pm = getActivity().getPackageManager();
               View v=super.getView(pos, convertView, parent);
               TextView tv = (TextView)v.findViewById(R.id.appn_name);
               ImageView icon = (ImageView)v.findViewById(R.id.app_icon);
               ResolveInfo ri = getItem(pos);
               tv.setText(ri.loadLabel(pm));
               icon.setImageDrawable(ri.loadIcon(pm));
               return v;
           }

        };

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo)l.getAdapter().getItem(position);
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        if(activityInfo == null) return;

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
