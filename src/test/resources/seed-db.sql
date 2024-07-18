-- Populate Address table
INSERT INTO address (id, city, state, country)
VALUES (1, 'New York', 'New York', 'USA');
INSERT INTO address (id, state, country)
VALUES (2, 'California', 'USA');
INSERT INTO address (id, city, state, country)
VALUES (3, 'Toronto', 'Ontario', 'Canada');

-- Populate User table
INSERT INTO user (id, email, password, registration_date, role_id)
VALUES (1, 'candidate@example.com', 'password', '2024-07-14', 1);
INSERT INTO user (id, email, password, role_id)
VALUES (2, 'recruiter@example.com', 'password', 2);
INSERT INTO user (id, email, password, registration_date, role_id)
VALUES (3, 'candidate2@example.com', 'pass456', '2024-07-13', 1);

-- Populate Company table
INSERT INTO company (id, name, has_logo, address_id)
VALUES (1, 'Tech Innovators Inc.', true, 1);
INSERT INTO company (id, name)
VALUES (2, 'Global Solutions Ltd.');
INSERT INTO company (id, name, has_logo)
VALUES (3, 'Creative Designs Co.', false);

-- Populate ContactInformation table
INSERT INTO contact_information (id, phone, twitter_handle, linkedin_handle, github_handle)
VALUES (1, '123-456-7890', 'https://twitter.com/user1', 'https://linkedin.com/in/user1',
        'https://github.com/user1');
INSERT INTO contact_information (id, phone)
VALUES (2, '987-654-3210');
INSERT INTO contact_information (id, linkedin_handle, github_handle)
VALUES (3, 'https://linkedin.com/in/user3', 'https://github.com/user3');

-- Populate CandidateProfile table
INSERT INTO candidate_profile (first_name, last_name, short_about, about, has_profile_photo,
                               has_resume, portfolio_website, work_authorization,
                               preferred_employment_type, user_id, address_id,
                               contact_information_id)
VALUES ('John', 'Doe', 'Experienced software developer',
        'Passionate about creating efficient and scalable solutions', true, true,
        'https://johndoe.com', 'US_CITIZEN', 'FULL_TIME', 1, 1, 1);

INSERT INTO candidate_profile (first_name, last_name, short_about, user_id,
                               contact_information_id)
VALUES ('Jane', 'Smith', 'Fresh graduate looking for opportunities', 2, 2);

INSERT INTO candidate_profile (first_name, last_name, short_about, work_authorization, user_id,
                               contact_information_id)
VALUES ('Mike', 'Johnson', 'Experienced project manager', 'H1_VISA', 3, 3);

-- Populate RecruiterProfile table
INSERT INTO recruiter_profile (first_name, last_name, about, has_profile_photo, user_id,
                               address_id, contact_information_id, company_id)
VALUES ('Sarah', 'Brown', 'Senior recruiter with 10 years of experience', true, 2, 2, 2, 1);

INSERT INTO recruiter_profile (first_name, last_name, user_id, company_id)
VALUES ('Tom', 'Wilson', 3, 2);


INSERT INTO recruiter_profile (first_name, last_name, has_profile_photo, user_id, company_id)
VALUES ('Emma', 'Davis', false, 1, 3);

-- Populate Job table
INSERT INTO job (id, title, description, salary, employment_type, remote_type, no_of_vacancy,
                 created_at, hiring_complete, recruiter_profile_id, address_id, company_id)
VALUES (1, 'Senior Software Engineer', 'Exciting opportunity for an experienced developer',
        '100000-120000', 'FULL_TIME', 'REMOTE_ONLY', 2, '2024-07-14', false, 1, 1, 1);

INSERT INTO job (id, title, description, employment_type, remote_type, no_of_vacancy,
                 recruiter_profile_id, company_id)
VALUES (2, 'Marketing Specialist', 'Join our growing marketing team', 'FULL_TIME', 'OFFICE_ONLY', 1,
        2, 2);

INSERT INTO job (id, title, description, employment_type, remote_type, no_of_vacancy,
                 recruiter_profile_id, company_id)
VALUES (3, 'UI/UX Designer Intern', 'Great opportunity for aspiring designers', 'INTERNSHIP',
        'PARTIAL_REMOTE', 3, 3, 3);

-- Populate CandidateJobApplication table
INSERT INTO candidate_job_application (id, candidate_profile_id, job_id, cover_letter)
VALUES (1, 1, 1,
        'I am excited to apply for this position and believe my skills align well with your requirements.');

INSERT INTO candidate_job_application (id, candidate_profile_id, job_id, cover_letter)
VALUES (2, 2, 2,
        'As a recent graduate, I am eager to contribute my fresh perspective to your marketing team.');

INSERT INTO candidate_job_application (id, candidate_profile_id, job_id, cover_letter)
VALUES (3, 3, 3,
        'I am passionate about UI/UX design and would love the opportunity to learn and grow with your company.');

-- Populate CandidateBookmarkedJob table
INSERT INTO candidate_bookmarked_job (id, candidate_profile_user_id, job_id)
VALUES (1, 1, 2);
INSERT INTO candidate_bookmarked_job (id, candidate_profile_user_id, job_id)
VALUES (2, 2, 1);
INSERT INTO candidate_bookmarked_job (id, candidate_profile_user_id, job_id)
VALUES (3, 3, 3);

-- Populate Education table
INSERT INTO education (id, title, description)
VALUES (1, 'Bachelor of Science in Computer Science', 'Graduated with honors from XYZ University');
INSERT INTO education (id, title, description)
VALUES (2, 'Master of Business Administration',
        'Specialized in Marketing from ABC Business School');
INSERT INTO education (id, title, description)
VALUES (3, 'Associate Degree in Graphic Design', 'Completed from Design Institute');

-- Populate Interest table
INSERT INTO interest (id, title)
VALUES (1, 'Artificial Intelligence');
INSERT INTO interest (id, title)
VALUES (2, 'Digital Marketing');
INSERT INTO interest (id, title)
VALUES (3, 'User Experience Design');

-- Populate Skill table
INSERT INTO skill (id, name, years_of_experience, experience_level, candidate_profile_id)
VALUES (1, 'Java Programming', '5+', 'ADVANCE', 1);

INSERT INTO skill (id, name, years_of_experience, experience_level, candidate_profile_id)
VALUES (2, 'Content Marketing', '2', 'INTERMEDIATE', 2);

INSERT INTO skill (id, name, years_of_experience, experience_level, candidate_profile_id)
VALUES (3, 'Adobe Photoshop', '1', 'BEGINNER', 3);

-- Add interest to recruiter with id 3
INSERT INTO recruiter_interest(recruiter_id, id)
VALUES (2, 1);
-- Add interest to candidate with id 1
INSERT INTO candidate_interest(candidate_id, id)
VALUES (1, 2);
