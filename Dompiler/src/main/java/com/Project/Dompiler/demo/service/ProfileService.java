package com.Project.Dompiler.demo.service;

import com.Project.Dompiler.demo.beans.Profile;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ProfileService {

	Profile getProfileById(String id);

	void deleteById(String pid);

	List<Profile> getProfiles();

	void updateProfle(String pid, String firstName, String lastName, String emailId, Date dob, String country, String city, String bio);

	Profile getProfileByEmailId(String emailId);

	void createProfile(String firstName, String lastName, String emailId, Date dob, String country, String city,
			String bio);

	void save(Profile profile);

}
