package org.grant.allocator;
import java.util.Random;
import java.util.HashSet;

public class addressAlloc {
    private final int[] storage;
    private final int copy;

    public addressAlloc(int[] storage, int copy) {
        this.storage = storage;
        this.copy = copy;
    }

    // 根据当前存储使用情况，返回应该放置下一个块的服务器IP_PORT地址
    public int[] getAddress() {
        int[] destinations = new int[copy + 1];
        Random rand = new Random();
        HashSet<Integer> selectedServers = new HashSet<>();

        // 随机选择一半的服务器地址
        int randomSelections = (copy+1) / 2;
        for (int i = 0; i < randomSelections; i++) {
            int serverIndex;
            do {
                serverIndex = rand.nextInt(storage.length);
            } while (selectedServers.contains(serverIndex));
            selectedServers.add(serverIndex);
            storage[serverIndex]++;
            destinations[i] = serverIndex;
        }

        // 从文件数量最少的服务器中选择剩余的地址
        for (int i = randomSelections; i < destinations.length; i++) {
            int leastUsedServer = findLeastUsedServer(selectedServers);
            selectedServers.add(leastUsedServer);
            destinations[i] = leastUsedServer;
        }

        return destinations;
    }


    // 查找当前存储使用量最少且未被选中的服务器
    private int findLeastUsedServer(HashSet<Integer> excludedServers) {
        int minIndex = -1;
        for (int i = 0; i < storage.length; i++) {
            if (!excludedServers.contains(i) && (minIndex == -1 || storage[i] < storage[minIndex])) {
                minIndex = i;
            }
        }
        storage[minIndex]++;
        return minIndex;
    }


    // 测试功能，目前是7服务器，3副本
    public static void main(String[] args){
        int servers = 3;
        int fileNum = 7;
        addressAlloc a = new addressAlloc(new int[servers], 1);
        int[][] temp = new int[fileNum][];
        for(int i = 0;i<fileNum;i++) {
            temp[i] = a.getAddress();
        }
        int[] num = new int[servers];
        for (int[] i : temp) {
            for(int j:i){
                System.out.print(j + " ");
                num[j]++;
            }
            System.out.println();
        }
        for(int j:num){
            System.out.print(j + " ");
        }

    }



}
