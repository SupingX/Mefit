package com.mycj.junsda.base;

import android.support.v4.app.Fragment;
import android.util.Log;

public class BaseFragment extends Fragment {

	public boolean isConnected() {
		BaseApp app = (BaseApp) getActivity().getApplication();
		boolean is =  app.getBlueService() != null
				&& app.getBlueService().isConnect();
		Log.i(getClass().getSimpleName(), "isConnected" + is);
		return is;
	}
}
