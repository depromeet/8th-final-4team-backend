package com.month.domain.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Uuid {

	private static final String version = "v1";

	@Column(nullable = false)
	private String uuid;

	private Uuid(String uuid) {
		this.uuid = uuid;
	}

	public static Uuid newInstance() {
		return new Uuid(String.format("%s-%s", version, UUID.randomUUID()));
	}

	public static Uuid of(String uuid) {
		return new Uuid(uuid);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Uuid uuid1 = (Uuid) o;
		return Objects.equals(uuid, uuid1.uuid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

}