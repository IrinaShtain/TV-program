package com.shtainyky.tvprogram.navigationdrawerfragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shtainyky.tvprogram.HttpManager;
import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.Program;
import com.shtainyky.tvprogram.parser.Parse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TVProgramViewPagerFragment extends Fragment {
    private int position;
    private RecyclerView mTVProgramRecyclerView;
    private TVProgramAdapter mAdapter;
    private TextView textView;
    private String result;

    public TVProgramViewPagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vp_tvprograms, container, false);
        textView = (TextView) view.findViewById(R.id.vp_textView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position = bundle.getInt("position");
            String temp = "Position = " + position;
            textView.setText(temp);
        }

        mTVProgramRecyclerView = (RecyclerView) view
                .findViewById(R.id.tvprogram_recycler_view);
        mTVProgramRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        //will be fixed
        List<Program> programs = new ArrayList<>();
        mAdapter = new TVProgramAdapter(programs);
        mTVProgramRecyclerView.setAdapter(mAdapter);
        // will delete
        Button button = (Button) view.findViewById(R.id.getData);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    Toast.makeText(getContext(), getString(R.string.internet), Toast.LENGTH_SHORT).show();
                    Calendar date = new GregorianCalendar();
                    String uri = "http://52.50.138.211:8080/ChanelAPI/programs/" + date.getTimeInMillis();
                    requestData(uri);
                    //new MyTask().execute(uri);
                } else {
                    Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

//

        return view;
    }

    private void requestData(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        result = Parse.parseJSONtoString(response);
                        textView.setText(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

    protected boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

//    public class MyTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            String content = HttpManager.getData(params[0]);
//            return content;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            String s1 = Parse.parseJSONtoString(s);
//            textView.setText(s1);
//        }
//    }

    private class TVProgramHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mTimeTextView;
        TextView mDateTextView;

        TVProgramHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time);
            mDateTextView = (TextView) itemView.findViewById(R.id.date);
        }

        public void bindProgram(Program program) {
            mTitleTextView.setText(program.getTitle());
            mTimeTextView.setText(program.getTime());
            mDateTextView.setText(program.getDate());
        }
    }

    private class TVProgramAdapter extends RecyclerView.Adapter<TVProgramHolder> {
        private List<Program> mPrograms;

        public TVProgramAdapter(List<Program> programs) {
            mPrograms = programs;
        }

        @Override
        public TVProgramHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_program, parent, false);
            return new TVProgramHolder(view);
        }

        @Override
        public void onBindViewHolder(TVProgramHolder holder, int position) {
            Program program = mPrograms.get(position);
            holder.bindProgram(program);
        }

        @Override
        public int getItemCount() {
            return mPrograms.size();
        }
    }
}
