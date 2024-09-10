package mz.org.csaude.mentoring.workSchedule.rest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Collections;

import mz.org.csaude.mentoring.base.auth.LoginRequest;
import mz.org.csaude.mentoring.base.auth.LoginResponse;
import mz.org.csaude.mentoring.base.auth.SessionManager;
import mz.org.csaude.mentoring.base.service.BaseRestService;
import mz.org.csaude.mentoring.dto.role.UserRoleDTO;
import mz.org.csaude.mentoring.dto.user.UserDTO;
import mz.org.csaude.mentoring.listner.rest.RestResponseListener;
import mz.org.csaude.mentoring.model.user.User;
import mz.org.csaude.mentoring.service.metadata.SyncDataService;
import mz.org.csaude.mentoring.service.user.UserService;
import mz.org.csaude.mentoring.service.user.UserServiceImpl;
import mz.org.csaude.mentoring.service.user.UserSyncService;
import mz.org.csaude.mentoring.util.LifeCycleStatus;
import mz.org.csaude.mentoring.util.Utilities;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRestService extends BaseRestService implements UserSyncService {


    private SessionManager sessionManager;

    public UserRestService(Application application, User currentUser) {
        super(application, currentUser);
    }


    public void doOnlineLogin(RestResponseListener listener, boolean rememberMe) {
        this.sessionManager = new SessionManager(getApplication());

        SyncDataService syncDataService = getRetrofit().create(SyncDataService.class);

        LoginRequest loginRequest = new LoginRequest(currentUser.getPassword(), currentUser.getUserName());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(loginRequest));

        Call<LoginResponse> call = syncDataService.login(body);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    LoginResponse data = response.body();

                    // Move the database operations to a background thread
                    getServiceExecutor().execute(() -> {
                        try {
                            String error = validateUserData(data);

                            if (error != null) {
                                listener.doOnRestErrorResponse(error);
                                return;
                            }

                            // Save user information and handle session data in the background thread
                            saveUserAndSessionData(data, rememberMe);

                            // If the access token exists, save it
                            if (Utilities.stringHasValue(data.getAccess_token())) {
                                sessionManager.saveAuthToken(data.getAccess_token(), data.getRefresh_token(), data.getExpires_in());

                                // Notify success on the main thread
                                listener.doOnRestSucessResponse(getApplication().getAuthenticatedUser());
                            }

                        } catch (SQLException e) {
                            // Handle database errors on the main thread
                            listener.doOnRestErrorResponse("Database error: " + e.getMessage());
                        }
                    });
                } else {
                    listener.doOnRestErrorResponse(response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("USER LOGIN --", t.getMessage(), t);
                listener.doOnRestErrorResponse("Login failed: " + t.getMessage());
            }
        });
    }

    private String validateUserData(LoginResponse data) {
        if (!Utilities.listHasElements(data.getUserDTO().getUserRoleDTOS())) {
            return "O utilizador não tem perfil associado";
        } else if (!isMentor(data.getUserDTO())) {
            return "O utilizador não tem perfil de mentor associado";
        } else if (data.getUserDTO().getLifeCycleStatus().equals(LifeCycleStatus.INACTIVE)) {
            return "O utilizador está inativo, contacte o administrador.";
        }
        return null;
    }

    private void saveUserAndSessionData(LoginResponse data, boolean rememberMe) throws SQLException {
        // Save user data to the database in the background thread
        getApplication().getUserService().savedOrUpdateUser(new User(data.getUserDTO()));

        // Retrieve the saved user and update additional information
        User user = getApplication().getUserService().getByuuid(data.getUserDTO().getUuid());
        user.setEmployee(getApplication().getEmployeeService().getById(user.getEmployeeId()));

        // Set the authenticated user with the rememberMe flag
        getApplication().setAuthenticatedUser(user, rememberMe);
    }


    private boolean isMentor(UserDTO userDTO) {
        for (UserRoleDTO userRoleDTO : userDTO.getUserRoleDTOS()) {
            if (userRoleDTO.getRoleDTO().getCode().equals("HEALTH_FACILITY_MENTOR")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getByUuid(RestResponseListener<User> listener) {

        try {
            Call<UserDTO> call = syncDataService.getByuuid(getApplication().getUserService().getAll().get(0).getUuid());

            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.code() == 200) {
                        UserDTO data = response.body();
                        getServiceExecutor().execute(()-> {
                            try {
                                User user1 = new User(data);
                                UserService userService = getApplication().getUserService();
                                userService.savedOrUpdateUser(user1);
                                listener.doOnResponse(BaseRestService.REQUEST_SUCESS, Collections.singletonList(user1));
                            } catch (SQLException e) {
                                Log.e("USER FETCH --", e.getMessage(), e);
                                listener.doOnRestErrorResponse(e.getMessage());
                            }
                        });
                    }

                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.e("USER FETCH --", t.getMessage(), t);
                    listener.doOnRestErrorResponse(t.getMessage());
                }
            });
        } catch (SQLException e) {
            Log.e("USER FETCH --", e.getMessage(), e);
            listener.doOnRestErrorResponse(e.getMessage());
        }
    }
}
