package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "/basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    // @PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemName,
                       @RequestParam("price") Integer price,
                       @RequestParam("quantity") Integer quantity,
                       Model model) {

        Item item = new Item(itemName, price, quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);
        
        // ModelAttribute가 자동으로 모델에 추가해줌. 매핑된 이름을 통해
        // model.addAttribute("item", item);

        return "basic/item";
    }

    // 뒤에오는 Item에서 첫 글자만 소문자로 바꿔서 매핑함. @ModelAttribute("item)이 됨
    // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);

        return "basic/item";
    }

    // @ModelAttribute 또한 생략 가능함 -> 굳이..? 근데 알고는 있어야 하니
    // 상품 추가 후에, 새로고침 하면 POST 요청이 계속 들어가게 되는 치명적 오류가 발생
    // 따라서 뷰네임을 단순 반환하는 것이 아니라, 리다이렉트를 해주어야 한다.
    // @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);

        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);

        // + 연산은 URL 인코딩이 안돼서 위험함. 그래서 RedirectAttributes를 사용해야 함
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);


        // {itemId}가 RedirectAttributes의 "itemId" 값으로 치환됨.
        // status의 나머지는 URL의 쿼리 파라미터로 들어감
        // 원래 모델에 값을 담고, 뷰에서 꺼내야 하는데 status는 타임리프에서 직접 지원해서 뷰에서 -바로 꺼낼 수 있음.
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);

        return "basic/editForm";
    }

    @PostMapping("{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }

    // 테스트용 데이터 추가
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
