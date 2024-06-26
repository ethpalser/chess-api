package com.chess.api.controller;

import com.chess.api.dao.UserRepository;
import com.chess.api.data.User;
import com.chess.api.data.UserRole;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureDataMongo
class ResourceControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    // Including UserRepository to set up database before running user tests
    @Autowired
    private UserRepository userRepository;

    // Store of user ids to delete after test
    private List<String> testUserIds;

    @BeforeAll
    void setup() {
        // Setup MockMvc
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        this.testUserIds = new ArrayList<>();
        // Test Users
        User basicUser = new User("test-user", "test-password");
        User adminUser = new User("test-admin", "not-a-secure-password", UserRole.ADMIN());
        // Store IDs
        this.testUserIds.add(basicUser.getId().toString());
        this.testUserIds.add(adminUser.getId().toString());
        // Save to DB
        this.userRepository.save(basicUser);
        this.userRepository.save(adminUser);
    }

    @AfterAll
    void teardown() {
        this.userRepository.deleteAllById(testUserIds);
    }

    @Test
    @WithUserDetails(value = "test-user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenUserCredentials_whenInvokeUserAuthorizedEndpoint_thenReturn200() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test-user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenUserCredentials_whenInvokeAdminAuthorizedEndpoint_thenReturn403() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "test-admin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenAdminCredentials_whenInvokeUserReadWriteAuthorizedEndpoint_thenReturn200() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isOk());
    }

    @Test
    void givenUserDoesNotExist_whenInvokeAuthorizedEndpoint_thenReturn401() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser(setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenAnonymous_whenInvokeAuthorizedEndpoint_thenReturn401() throws Exception {
        mockMvc.perform(get("/user")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser(setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenAnonymous_whenInvokeUnauthorizedEndpoint_thenReturn200() throws Exception {
        mockMvc.perform(get("/anonymous")).andExpect(status().isOk());
    }

}
