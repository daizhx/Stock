package com.hengxuan.stock.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hengxuan.stock.Activity.LoginActivity;
import com.hengxuan.stock.Activity.SubscriptionActivity;
import com.hengxuan.stock.R;
import com.hengxuan.stock.user.User;
import com.hengxuan.stock.utils.Log;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Map<String,String>> list = new ArrayList<Map<String,String>>();;
    String[] keys=new String[]{
            "subject","hint"
    };

    private OnFragmentInteractionListener mListener;
    private TextView mTVUserId;
    private String userId = null;
    private Button mBtn_login;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_user_setting, container, false);
        TextView tvTitle = (TextView) root.findViewById(R.id.title_text);
        tvTitle.setText(R.string.member);
        final TextView userName = (TextView) root.findViewById(R.id.user_name);
        final View btn = root.findViewById(R.id.btn_login);
        final User user = User.getUser(getActivity());
        ListView listview = (ListView) root.findViewById(R.id.list);
        listview.setAdapter(new SimpleAdapter(getActivity(), list, R.layout.user_setting_item, keys, new int[]{R.id.text, R.id.second_text}));
//        listview.setEnabled(false);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                switch (arg2) {
                    case 0:
//                        Toast.makeText(getActivity(),R.string.not_use,Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getActivity(), SubscriptionActivity.class));
                        ChargeDialogFragment chargeDialogFragment = new ChargeDialogFragment();
                        chargeDialogFragment.show(getFragmentManager(),"dialog");
                        break;
                    case 1:
                        Toast.makeText(getActivity(),R.string.newest_version,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        if(user.isLogin()) {
                            user.hasLogout();
                            btn.setVisibility(View.VISIBLE);
                            userName.setVisibility(View.INVISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        if(user.isLogin()){
            btn.setVisibility(View.INVISIBLE);
            userName.setText(user.getName());
        }else{
            btn.setVisibility(View.VISIBLE);
        }
        mTVUserId = (TextView) root.findViewById(R.id.user_id);
        mTVUserId.setText("ID: "+userId);
        mBtn_login = (Button) root.findViewById(R.id.btn_login);
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return root;
    }


    /**
     * set listview data
     */
    private void setData(){
        HashMap<String, String> m = new HashMap<String, String>();
        m.put(keys[0], getString(R.string.buy_vip));
        list.add(m);
        m = new HashMap<String, String>();
        m.put(keys[0], getString(R.string.update));
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.hengxuan.stock",0);
            m.put(keys[1], getString(R.string.version_hint)+": "+info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        list.add(m);
//        m = new HashMap<String, String>();
//        m.put(keys[0], getString(R.string.about));
//        list.add(m);
        m = new HashMap<String, String>();
        m.put(keys[0], getString(R.string.log_out));
        list.add(m);

        userId = User.getUser(getActivity()).getId();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        list.clear();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
