package mt.mm.autograph.interfaces;

import okhttp3.Call;

/**
 * Created by wang_xu_xiao on 8/30/2017 4:34 PM.
 */

public interface FailureCallback {
    void onFailure(Call call,String msg);
}
