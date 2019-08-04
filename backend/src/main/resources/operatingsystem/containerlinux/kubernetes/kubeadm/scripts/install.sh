#!/usr/bin/env bash

ip=$1
host=$2

kubectl drain $host --delete-local-data --force --ignore-daemonsets
kubectl delete node $host
kubeadm reset -f
rm -r /var/lib/etcd
iptables -F && iptables -t nat -F && iptables -t mangle -F && iptables -X
ipvsadm -C

systemctl enable docker.service

CNI_VERSION="v0.7.5"
mkdir -p /opt/cni/bin
curl -L "https://github.com/containernetworking/plugins/releases/download/${CNI_VERSION}/cni-plugins-amd64-${CNI_VERSION}.tgz" | tar -C /opt/cni/bin -xz



CRICTL_VERSION="v1.12.0"
mkdir -p /opt/bin
curl -L "https://github.com/kubernetes-incubator/cri-tools/releases/download/${CRICTL_VERSION}/crictl-${CRICTL_VERSION}-linux-amd64.tar.gz" | tar -C /opt/bin -xz

echo "KUBELET_EXTRA_ARGS=--volume-plugin-dir=/var/lib/kubelet/volumeplugins" > /etc/default/kubelet

RELEASE="$(curl -sSL https://dl.k8s.io/release/stable.txt)"

mkdir -p /opt/bin
cd /opt/bin
curl -L --remote-name-all https://storage.googleapis.com/kubernetes-release/release/${RELEASE}/bin/linux/amd64/{kubeadm,kubelet,kubectl}
chmod +x {kubeadm,kubelet,kubectl}

curl -sSL "https://raw.githubusercontent.com/kubernetes/kubernetes/${RELEASE}/build/debs/kubelet.service" | sed "s:/usr/bin:/opt/bin:g" > /etc/systemd/system/kubelet.service
mkdir -p /etc/systemd/system/kubelet.service.d
curl -sSL "https://raw.githubusercontent.com/kubernetes/kubernetes/${RELEASE}/build/debs/10-kubeadm.conf" | sed "s:/usr/bin:/opt/bin:g" > /etc/systemd/system/kubelet.service.d/10-kubeadm.conf

systemctl enable --now kubelet

kubeadm config images pull
kubeadm init --apiserver-advertise-address=$ip --pod-network-cidr=10.244.0.0/16 --node-name=$host

rm -r $HOME/.kube
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

kubectl taint nodes --all node-role.kubernetes.io/master-
kubectl apply -f https://docs.projectcalico.org/v3.7/manifests/canal.yaml

kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta1/aio/deploy/recommended.yaml

rm -r /home/bornium/.kube
mkdir -p /home/bornium/.kube
sudo cp -i /etc/kubernetes/admin.conf /home/bornium/.kube/config
sudo chown 1000:1000 /home/bornium/.kube/config