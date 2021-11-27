package com.ta.kfc.mercu.service.security;

import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.Role;
import com.ta.kfc.mercu.infrastructure.db.orm.model.auth.User;
import com.ta.kfc.mercu.infrastructure.db.orm.model.security.Menu;

import java.util.List;

public interface AuthorizationService {
    List<Menu> getAuthorizedMenu();
}
