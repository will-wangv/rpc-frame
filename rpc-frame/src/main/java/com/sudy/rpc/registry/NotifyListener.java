package com.sudy.rpc.registry;

import java.net.URI;
import java.util.Set;

public interface NotifyListener {
    void notify(Set<URI> uris);
}